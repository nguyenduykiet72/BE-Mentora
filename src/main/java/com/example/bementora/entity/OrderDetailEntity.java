package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {
    @Id
    @Column(name = "orderDetailId", columnDefinition = "UUID")
    private UUID orderDetailId;

    @Column(name = "paymentId", columnDefinition = "UUID")
    private UUID paymentId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "finalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId", insertable = false, updatable = false)
    private PaymentEntity payment;
}
