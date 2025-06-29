package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_cart_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cartItemId", columnDefinition = "UUID")
    private UUID cartItemId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "cartId", columnDefinition = "UUID")
    private UUID cartId;

    @Column(name = "originalPrice", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "currentPrice", precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "appliedVoucherId", columnDefinition = "UUID")
    private UUID appliedVoucherId;

    @Column(name = "finalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "addedAt")
    private LocalDateTime addedAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // 🆕 Validation flags
    @Column(name = "isCourseStillAvailable")
    private Boolean isCourseStillAvailable = true;

    @Column(name = "isPriceChanged")
    private Boolean isPriceChanged = false;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId", insertable = false, updatable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliedVoucherId", insertable = false, updatable = false)
    private VoucherEntity appliedVoucher;

    public boolean hasDiscount() {
        return discount != null && discount.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getDiscountPercentage() {
        if (originalPrice == null || originalPrice.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return discount.divide(originalPrice,4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
}