package com.example.bementora.dto.request;

import com.example.bementora.enums.CurriculumEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CurriculumCreationRequest{
    @NotNull(message = "Course ID is required")
    private UUID moduleId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Order index is required")
    private Integer orderIndex;

    @NotNull(message = "Type is required")
    private CurriculumEnum type; // LECTURE or QUIZ
}
