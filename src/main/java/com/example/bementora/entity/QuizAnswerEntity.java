package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_quiz_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerEntity {
    @Id
    @Column(name = "userAnswerId", columnDefinition = "UUID")
    private UUID userAnswerId;

    @Column(name = "attemptId", columnDefinition = "UUID")
    private UUID attemptId;

    @Column(name = "questionId", columnDefinition = "UUID")
    private UUID questionId;

    @Column(name = "answerId", columnDefinition = "UUID")
    private UUID answerId;

    @Column(name = "isCorrect")
    private Boolean isCorrect;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attemptId", insertable = false, updatable = false)
    private QuizAttemptEntity quizAttempt;
}