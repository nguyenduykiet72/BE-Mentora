package com.example.bementora.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID cartId;
    private int itemCount;
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
    private LocalDateTime lastUpdated;
    private boolean hasExpiredItems;
    private boolean hasPriceChanges;
}
