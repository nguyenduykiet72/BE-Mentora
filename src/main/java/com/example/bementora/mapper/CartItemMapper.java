package com.example.bementora.mapper;

import com.example.bementora.dto.request.AddToCartRequest;
import com.example.bementora.entity.CartItemEntity;
import com.example.bementora.entity.CoursesEntity;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    default CartItemEntity createCartItem(UUID cartId, CoursesEntity course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCartId(cartId);
        cartItem.setCourseId(course.getCourseId());
        cartItem.setOriginalPrice(course.getPrice());
        cartItem.setCurrentPrice(course.getPrice());
        cartItem.setFinalPrice(course.getPrice());
        cartItem.setDiscount(BigDecimal.ZERO);
        cartItem.setAddedAt(LocalDateTime.now());
        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItem.setIsCourseStillAvailable(Boolean.TRUE);
        cartItem.setIsPriceChanged(Boolean.FALSE);
        return cartItem;
    }

    default CartItemEntity createCartItemFromRequest(AddToCartRequest request, UUID cartId, CoursesEntity course) {
        return createCartItem(cartId, course);
    }
}