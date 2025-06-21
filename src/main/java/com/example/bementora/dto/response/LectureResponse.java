package com.example.bementora.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
public class LectureResponse {
    private UUID lectureId;
    private UUID curriculumId;
    private String title;
    private String description;
    private String videoUrl; // Presigned URL for viewing
    private String videoS3Key; // Original S3 key
    private String articleContent;
    private Integer duration;
    private Boolean isFree;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}