package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_quizzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizEntity {
    @Id
    @Column(name = "quizId", columnDefinition = "UUID")
    private UUID quizId;

    @Column(name = "curriculumId", columnDefinition = "UUID")
    private UUID curriculumId;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "passingScore")
    private Integer passingScore = 70;

    @Column(name = "timeLimit")
    private Integer timeLimit;

    @Column(name = "isFree")
    private Boolean isFree = false;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizAttemptEntity> quizAttempts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
    private CurriculumEntity curriculum;
}
