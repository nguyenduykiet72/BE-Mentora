package com.example.bementora.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartItemResponse {
    private UUID cartItemId;
    private UUID courseId;
    private String courseTitle;
    private String courseThumbnail;
    private String instructorName;
    private BigDecimal originalPrice;
    private BigDecimal currentPrice;
    private BigDecimal discount;
    private BigDecimal finalPrice;
    private String appliedVoucherCode;
    private LocalDateTime addedAt;
    private boolean isPriceChanged;
    private boolean isCourseStillAvailable;
}
