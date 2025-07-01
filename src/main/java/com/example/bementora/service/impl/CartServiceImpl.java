package com.example.bementora.service.impl;

import com.example.bementora.dto.request.AddToCartRequest;
import com.example.bementora.dto.response.CartResponse;
import com.example.bementora.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    @Override
    public CartResponse getCart(UUID userId, String sessionId) {
        return null;
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        return null;
    }

    @Override
    public CartResponse removeFromCart(UUID cartItemId, UUID userId, String sessionId) {
        return null;
    }

    @Override
    public CartResponse updateCartItem(UUID cartItemId, AddToCartRequest request) {
        return null;
    }

    @Override
    public CartResponse applyVoucher(UUID userId, String sessionId, String voucherCode) {
        return null;
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
        return false;
    }

    @Override
    public boolean isCourseInCart(UUID userId, String sessionId, UUID courseId) {
        return false;
    }
}
