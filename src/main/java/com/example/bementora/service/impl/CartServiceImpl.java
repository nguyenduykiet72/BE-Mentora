package com.example.bementora.service.impl;

import com.example.bementora.dto.request.AddToCartRequest;
import com.example.bementora.dto.response.CartResponse;
import com.example.bementora.entity.CartEntity;
import com.example.bementora.entity.CartItemEntity;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.entity.VoucherEntity;
import com.example.bementora.enums.CartStatus;
import com.example.bementora.exception.CartItemNotFoundException;
import com.example.bementora.exception.CourseAlreadyPurchasedException;
import com.example.bementora.exception.CourseNotFoundException;
import com.example.bementora.exception.VoucherNotFoundException;
import com.example.bementora.mapper.CartItemMapper;
import com.example.bementora.mapper.CartMapper;
import com.example.bementora.repository.*;
import com.example.bementora.service.CartService;
import com.example.bementora.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CourseRepository courseRepository;
    private final VoucherRepository voucherRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final RedisService redisService;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    private static final int ANONYMOUS_CART_EXPIRY_HOURS = 24;
    private static final String CART_CACHE_PREFIX = "cart:user:";
    private static final int CART_CACHE_TTL = 3600; // 1 hour

    @Override
    @Transactional
    public CartResponse getCart(UUID userId, String sessionId) {
        CartEntity cart = findOrCreateCart(userId, sessionId);

        validateCartItems(cart);

        if (userId != null) {
            cacheCartData(cart);
        }

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        CoursesEntity course = validateCourseForCart(request.getCourseId(),request.getUserId());

        CartEntity cart = findOrCreateCart(request.getUserId(),request.getSessionId());

        if (isCourseInCart(request.getUserId(), request.getSessionId(), request.getCourseId())) {
            log.info("Course {} already in cart for user/session", request.getCourseId());
            return cartMapper.toCartResponse(cart);
        }

        CartItemEntity cartItem = cartItemMapper.createCartItem(cart.getCartId(), course);

        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            applyVoucherToCartItem(cartItem, request.getVoucherCode(), course);
        }

        cartItemRepository.save(cartItem);

        cart = reloadCartWithItems(cart.getCartId());

        recalculateCartTotals(cart);
        cartRepository.save(cart);

        if (request.getUserId() != null) {
            cacheCartData(cart);
        }

        log.info("Added course {} to cart {} for user/session",
                request.getCourseId(), cart.getCartId());

        return cartMapper.toCartResponse(cart);

    }

    @Override
    public CartResponse removeFromCart(UUID cartItemId, UUID userId, String sessionId) {
        CartEntity cart = findOrCreateCart(userId,sessionId);

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new CartItemNotFoundException("Cart item does not belong to this cart");
        }

        cartItemRepository.delete(cartItem);

        cart = reloadCartWithItems(cart.getCartId());
        recalculateCartTotals(cart);
        cartRepository.save(cart);

        if (userId != null) {
            cacheCartData(cart);
        }

        log.info("Removed cart item {} from cart {}", cartItemId, cart.getCartId());

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(UUID cartItemId, AddToCartRequest request) {
        CartEntity cart = findOrCreateCart(request.getUserId(),request.getSessionId());

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new CartItemNotFoundException("Cart item does not belong to this cart");
        }

        CoursesEntity course = validateCourseForCart(request.getCourseId(), request.getUserId());

        if (!cartItem.getCurrentPrice().equals(course.getPrice())) {
            cartItem.setCurrentPrice(course.getPrice());
            cartItem.setFinalPrice(course.getPrice().subtract(cartItem.getDiscount()));
        }

        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            applyVoucherToCartItem(cartItem, request.getVoucherCode(), course);
        } else {
            removeVoucherFromCartItem(cartItem);
        }

        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(cartItem);

        cart = reloadCartWithItems(cart.getCartId());
        recalculateCartTotals(cart);
        cartRepository.save(cart);

        if (request.getUserId() != null) {
            cacheCartData(cart);
        }

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse applyVoucher(UUID userId, String sessionId, String voucherCode) {
       CartEntity cart = findOrCreateCart(userId,sessionId);

        if (cart.getCartItemEntities() == null || cart.getCartItemEntities().isEmpty()) {
            return cartMapper.toCartResponse(cart);
        }

        boolean hasChanges = false;

        for (CartItemEntity cartItem : cart.getCartItemEntities()) {
            CoursesEntity course = courseRepository.findById(cartItem.getCourseId()).orElse(null);
            if (course != null) {
                try {
                    applyVoucherToCartItem(cartItem, voucherCode, course);
                    cartItemRepository.save(cartItem);
                    hasChanges = true;
                } catch (Exception e) {
                    log.warn("Failed to apply voucher {} to cart item {}: {}",
                            voucherCode, cartItem.getCartItemId(), e.getMessage());
                }
            }
        }

        if (hasChanges) {
            cart = reloadCartWithItems(cart.getCartId());
            recalculateCartTotals(cart);
            cartRepository.save(cart);

            if (userId != null) {
                cacheCartData(cart);
            }
        }

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse removeVoucher(UUID userId, String sessionId, UUID cartItemId) {
        return null;
    }

    @Override
    public CartResponse mergeAnonymousCart(String sessionId, UUID userId) {
        return null;
    }

    @Override
    public void syncCartToCache(UUID userId) {

    }

    @Override
    public void cleanupExpiredCarts() {

    }

    @Override
    public CartResponse validateCart(UUID userId, String sessionId) {
        return null;
    }

    @Override
    public boolean isUserAlreadyEnrolled(UUID userId, UUID courseId) {
        return courseEnrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public boolean isCourseInCart(UUID userId, String sessionId, UUID courseId) {
        CartEntity cart = findOrCreateCart(userId, sessionId);
        return cart.getCartItemEntities().stream()
                .anyMatch(item -> item.getCourseId().equals(courseId));
    }

    private CartEntity findOrCreateCart(UUID userId, String sessionId) {
        Optional<CartEntity> existingCart;

        if (userId != null) {
            existingCart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
        } else {
            existingCart = cartRepository.findBySessionIdAndStatus(userId, CartStatus.ACTIVE);
        }

        if (existingCart.isPresent() && !existingCart.get().isExpired()) {
            return existingCart.get();
        }

        return createNewCart(userId, sessionId);
    }

    private CartEntity createNewCart(UUID userId, String sessionId) {
        CartEntity cart = new CartEntity();
        cart.setCartId(UUID.randomUUID());
        cart.setUserId(userId);
        cart.setSessionId(sessionId);
        cart.setCartStatus(CartStatus.ACTIVE);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        if (userId == null) {
            cart.setExpiresAt(LocalDateTime.now().plusHours(ANONYMOUS_CART_EXPIRY_HOURS));
        }

        return cartRepository.save(cart);
    }

    private void cacheCartData(CartEntity cart) {
        String cacheKey = "cart:user:" + cart.getUserId();
        redisService.setWithExpiry(cacheKey, cart, 3600);
    }

    private void recalculateCartTotals(CartEntity cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (CartItemEntity item : cart.getCartItemEntities()) {
            subtotal = subtotal.add(item.getOriginalPrice());
            totalDiscount = totalDiscount.add(item.getDiscount());
        }

        cart.setSubtotal(subtotal);
        cart.setTotalDiscount(totalDiscount);
        cart.setTotalAmount(subtotal.subtract(totalDiscount));
        cart.setUpdatedAt(LocalDateTime.now());
    }

    private CoursesEntity validateCourseForCart(UUID courseId, UUID userId) {
        CoursesEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        if (userId != null && isUserAlreadyEnrolled(userId, courseId)) {
            throw new CourseAlreadyPurchasedException("User already enrolled in this course");
        }

        return course;
    }

    private CartEntity reloadCartWithItems(UUID cartId) {
        return cartRepository.findActiveCartWithItemsByUserId(cartId)
                .orElseGet(() -> cartRepository.findById(cartId).orElse(null));
    }

    private void applyVoucherToCartItem(CartItemEntity cartItem, String voucherCode, CoursesEntity course) {
        Optional<VoucherEntity> voucherOpt = voucherRepository.findValidVoucherForCourse(
                voucherCode, course.getCourseId(), LocalDateTime.now());

        if (voucherOpt.isEmpty()) {
            throw new VoucherNotFoundException("Voucher not found or not applicable to this course");
        }

        VoucherEntity voucher = voucherOpt.get();
        BigDecimal discount = calculateVoucherDiscount(voucher, cartItem.getCurrentPrice());

        cartItem.setAppliedVoucherId(voucher.getVoucherId());
        cartItem.setDiscount(discount);
        cartItem.setFinalPrice(cartItem.getCurrentPrice().subtract(discount));
        cartItem.setUpdatedAt(LocalDateTime.now());

        log.info("Applied voucher {} to cart item {}, discount: {}",
                voucherCode, cartItem.getCartItemId(), discount);
    }


    private void removeVoucherFromCartItem(CartItemEntity cartItem) {
        cartItem.setAppliedVoucherId(null);
        cartItem.setDiscount(BigDecimal.ZERO);
        cartItem.setFinalPrice(cartItem.getCurrentPrice());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }


    private void validateCartItems(CartEntity cart) {
        if (cart.getCartItemEntities() == null || cart.getCartItemEntities().isEmpty()) {
            return;
        }

        boolean hasChanges = false;

        for (CartItemEntity cartItem : cart.getCartItemEntities()) {
            Optional<CoursesEntity> courseOpt = courseRepository.findById(cartItem.getCourseId());

            if (courseOpt.isEmpty()) {
                cartItem.setIsCourseStillAvailable(false);
                hasChanges = true;
                log.warn("Course {} not found for cart item {}", cartItem.getCourseId(), cartItem.getCartItemId());
                continue;
            }

            CoursesEntity course = courseOpt.get();

            if (!cartItem.getCurrentPrice().equals(course.getPrice())) {
                cartItem.setOriginalPrice(cartItem.getCurrentPrice());
                cartItem.setCurrentPrice(course.getPrice());
                cartItem.setIsPriceChanged(true);

                BigDecimal newFinalPrice = course.getPrice().subtract(cartItem.getDiscount());
                cartItem.setFinalPrice(newFinalPrice.max(BigDecimal.ZERO));

                hasChanges = true;
                log.info("Price changed for course {} in cart item {}: {} -> {}",
                        course.getCourseId(), cartItem.getCartItemId(),
                        cartItem.getOriginalPrice(), course.getPrice());
            }

            if (cart.getUserId() != null &&
                    courseEnrollmentRepository.existsByUserIdAndCourseId(cart.getUserId(), cartItem.getCourseId())) {

                cartItemRepository.delete(cartItem);
                hasChanges = true;
                log.info("Removed course {} from cart - user {} already enrolled",
                        cartItem.getCourseId(), cart.getUserId());
                continue;
            }

            if (cartItem.getAppliedVoucherId() != null) {
                validateCartItemVoucher(cartItem, course);
                hasChanges = true;
            }
        }

        if (hasChanges) {
            // Refresh cart items after changes
            cart.setCartItemEntities(cartItemRepository.findByCartIdWithCourse(cart.getCartId()));

            recalculateCartTotals(cart);

            cartRepository.save(cart);

            log.info("Cart {} validated and updated", cart.getCartId());
        }
    }

    private void validateCartItemVoucher(CartItemEntity cartItem, CoursesEntity course) {
        Optional<VoucherEntity> voucherOpt = voucherRepository.findValidVoucherForCourse(
                cartItem.getAppliedVoucher() != null ? cartItem.getAppliedVoucher().getCode() : null,
                course.getCourseId(),
                LocalDateTime.now()
        );

        if (voucherOpt.isEmpty()) {
            cartItem.setAppliedVoucherId(null);
            cartItem.setDiscount(BigDecimal.ZERO);
            cartItem.setFinalPrice(cartItem.getCurrentPrice());

            log.warn("Removed invalid voucher from cart item {}", cartItem.getCartItemId());
        } else {
            // Voucher vẫn valid - recalculate discount
            VoucherEntity voucher = voucherOpt.get();
            BigDecimal discount = calculateVoucherDiscount(voucher, cartItem.getCurrentPrice());

            cartItem.setDiscount(discount);
            cartItem.setFinalPrice(cartItem.getCurrentPrice().subtract(discount));

            log.info("Updated voucher discount for cart item {}: {}",
                    cartItem.getCartItemId(), discount);
        }
    }

    private BigDecimal calculateVoucherDiscount(VoucherEntity voucher, BigDecimal originalPrice) {
        BigDecimal discount = BigDecimal.ZERO;

        if ("Percentage".equals(voucher.getDiscountType())) {
            discount = originalPrice.multiply(voucher.getDiscountValue())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            if (voucher.getMaxDiscount() != null && discount.compareTo(voucher.getMaxDiscount()) > 0) {
                discount = voucher.getMaxDiscount();
            }
        } else if ("Fixed".equals(voucher.getDiscountType())) {
            discount = voucher.getDiscountValue();

            if (discount.compareTo(originalPrice) > 0) {
                discount = originalPrice;
            }
        }

        return discount;
    }

}
