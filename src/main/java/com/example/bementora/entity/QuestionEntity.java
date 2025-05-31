package com.example.bementora.entity;

import com.example.bementora.enums.QuestionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Id
    @Column(name = "questionId", columnDefinition = "UUID")
    private UUID questionId;

    @Column(name = "quizId", columnDefinition = "UUID")
    private UUID quizId;

    @Column(name = "questionText", columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "questionType")
    private QuestionTypeEnum questionType;

    @Column(name = "orderIndex")
    private Integer orderIndex;

    @Column(name = "points")
    private Integer points = 1;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerEntity> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizId", insertable = false, updatable = false)
    private QuizEntity quiz;
}
