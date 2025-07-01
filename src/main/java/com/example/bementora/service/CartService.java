package com.example.bementora.service;

import com.example.bementora.dto.request.AddToCartRequest;
import com.example.bementora.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse getCart(UUID userId, String sessionId);
    CartResponse addToCart(AddToCartRequest request);
    CartResponse removeFromCart(UUID cartItemId, UUID userId, String sessionId);
    CartResponse updateCartItem(UUID cartItemId, AddToCartRequest request);

    // voucher
    CartResponse applyVoucher(UUID userId, String sessionId, String voucherCode);
    CartResponse removeVoucher(UUID userId, String sessionId, UUID cartItemId);
    // migrate and sync cart
    CartResponse mergeAnonymousCart(String sessionId, UUID userId);
    void syncCartToCache(UUID userId);
    void cleanupExpiredCarts();

    // validate for cart
    CartResponse validateCart(UUID userId, String sessionId);
    boolean isUserAlreadyEnrolled(UUID userId, UUID courseId);
    boolean isCourseInCart(UUID userId, String sessionId, UUID courseId);

}
