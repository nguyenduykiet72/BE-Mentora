package com.example.bementora.entity;

import com.example.bementora.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_quiz_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptEntity {
    @Id
    @Column(name = "attemptId", columnDefinition = "UUID")
    private UUID attemptId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "quizId", columnDefinition = "UUID")
    private UUID quizId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "isPassed")
    private Boolean isPassed;

    @Column(name = "startedAt")
    private LocalDateTime startedAt;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<QuizAnswerEntity> quizAnswers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizId", insertable = false, updatable = false)
    private QuizEntity quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}