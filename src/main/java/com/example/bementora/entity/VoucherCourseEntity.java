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
@Table(name = "tbl_voucher_courses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherCourseEntity {
    @Id
    @Column(name = "voucherCourseId", columnDefinition = "UUID")
    private UUID voucherCourseId;

    @Column(name = "voucherId", columnDefinition = "UUID")
    private UUID voucherId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "originalPrice", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "discountAmount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "finalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "maxUsageCount")
    private Integer maxUsageCount = 0;

    @Column(name = "currentUsage")
    private Integer currentUsage = 0;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucherId", insertable = false, updatable = false)
    private VoucherEntity voucher;
}
