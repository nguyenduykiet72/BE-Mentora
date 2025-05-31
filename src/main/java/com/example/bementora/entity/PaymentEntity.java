package com.example.bementora.entity;

import com.example.bementora.enums.PaymentEnum;
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
@Table(name = "tbl_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @Column(name = "paymentId", columnDefinition = "UUID")
    private UUID paymentId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentEnum status;

    @Column(name = "transactionId")
    private String transactionId;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    // Relationships
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<OrderDetailEntity> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
