package com.example.bementora.entity;

import com.example.bementora.enums.QuestionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntity {
    @Id
    @Column(name = "answerId", columnDefinition = "UUID")
    private UUID answerId;

    @Column(name = "questionId", columnDefinition = "UUID")
    private UUID questionId;

    @Column(name = "answerText", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "isCorrect")
    private Boolean isCorrect;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId", insertable = false, updatable = false)
    private QuestionEntity question;
}
