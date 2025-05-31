package com.example.bementora.entity;

import com.example.bementora.enums.VoucherScopeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_vouchers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherEntity {
    @Id
    @Column(name = "voucherId", columnDefinition = "UUID")
    private UUID voucherId;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private VoucherScopeEnum scope;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "creatorId", columnDefinition = "UUID")
    private UUID creatorId;

    @Column(name = "creatorRole")
    private String creatorRole;

    @Column(name = "categoryId", columnDefinition = "UUID")
    private UUID categoryId;

    @Column(name = "discountType")
    private String discountType = "Percentage";

    @Column(name = "discountValue", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "maxDiscount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @Column(name = "maxUsage")
    private Integer maxUsage;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<VoucherCourseEntity> voucherCourses;

}
