package com.example.bementora.dto.response;

import com.example.bementora.enums.CurriculumEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CurriculumResponse {
    private UUID curriculumId;
    private UUID moduleId;
    private String title;
    private String description;
    private CurriculumEnum type;
    private Integer orderIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dynamic content based on type
    private List<LectureResponse> lectures;
    private List<QuizResponse> quizzes;
}
