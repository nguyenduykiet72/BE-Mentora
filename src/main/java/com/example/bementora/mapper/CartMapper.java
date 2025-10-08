package com.example.bementora.mapper;

import com.example.bementora.dto.response.CartItemResponse;
import com.example.bementora.dto.response.CartResponse;
import com.example.bementora.entity.CartEntity;
import com.example.bementora.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "itemCount", source = "itemCount")
    @Mapping(target = "items", source = "cartItemEntities")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    @Mapping(target = "hasExpiredItems", expression = "java(checkHasExpiredItems(cart))")
    @Mapping(target = "hasPriceChanges", expression = "java(checkHasPriceChanges(cart))")
    CartResponse toCartResponse(CartEntity cart);

    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "courseThumbnail", source = "course.thumbnail")
    @Mapping(target = "instructorName", source = "course.instructor.instructorName")
    @Mapping(target = "appliedVoucherCode", source = "appliedVoucher.code")
    CartItemResponse toCartItemResponse(CartItemEntity cartItem);

    List<CartItemResponse> toCartItemResponseList(List<CartItemEntity> cartItems);

    // Helper methods
    default boolean checkHasExpiredItems(CartEntity cart) {
        return cart.getCartItemEntities() != null &&
                cart.getCartItemEntities().stream()
                        .anyMatch(item -> Boolean.FALSE.equals(item.getIsCourseStillAvailable()));
    }

    default boolean checkHasPriceChanges(CartEntity cart) {
        return cart.getCartItemEntities() != null &&
                cart.getCartItemEntities().stream()
                        .anyMatch(item -> Boolean.TRUE.equals(item.getIsPriceChanged()));
    }
}
