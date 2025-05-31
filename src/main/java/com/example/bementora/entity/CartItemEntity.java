package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_cart_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity {
    @Id
    @Column(name = "cartItemId", columnDefinition = "UUID")
    private UUID cartItemId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "cartId", columnDefinition = "UUID")
    private UUID cartId;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "appliedVoucherId", columnDefinition = "UUID")
    private UUID appliedVoucherId;

    @Column(name = "finalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId", insertable = false, updatable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;
}