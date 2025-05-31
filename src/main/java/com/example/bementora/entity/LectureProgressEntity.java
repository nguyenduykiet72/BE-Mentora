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
@Table(name = "tbl_lecture_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureProgressEntity {
    @Id
    @Column(name = "progressId", columnDefinition = "UUID")
    private UUID progressId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "lectureId", columnDefinition = "UUID")
    private UUID lectureId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressEnum status;

    @Column(name = "lastPosition")
    private Integer lastPosition = 0;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectureId", insertable = false, updatable = false)
    private LectureEntity lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
