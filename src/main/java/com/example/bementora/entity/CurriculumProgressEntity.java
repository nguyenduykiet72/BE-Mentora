package com.example.bementora.entity;

import com.example.bementora.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_curriculum_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumProgressEntity {
    @Id
    @Column(name = "progressId", columnDefinition = "UUID")
    private UUID progressId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "curriculumId", columnDefinition = "UUID")
    private UUID curriculumId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressEnum status = ProgressEnum.NOT_STARTED;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
    private CurriculumEntity curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
