package com.example.bementora.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuizCreationRequest {
    @NotNull(message = "Curriculum ID is required")
    private UUID curriculumId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Integer passingScore = 70;

    private Integer timeLimit; // in minutes

    private Boolean isFree = false;

//    private List<QuestionCreationTime> questions;
}
