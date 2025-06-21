package com.example.bementora.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LectureCreationRequest {

    @NotNull(message = "Course ID is required")
    private UUID curriculumId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String videoUrl; // s3 key after upload

    private String articleContent;

    private Integer durationTime; // seconds

    private Boolean isFree = false;
}
