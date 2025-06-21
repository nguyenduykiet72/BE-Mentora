package com.example.bementora.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class QuizResponse {
    private UUID quizId;
    private UUID curriculumId;
    private String title;
    private String description;
    private Integer passingScore;
    private Integer timeLimit;
    private Boolean isFree;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer questionCount;
//    private List<QuestionResponse> questions; // Optional, for detailed view
}
