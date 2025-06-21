package com.example.bementora.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ModuleResponse {
    private UUID moduleId;
    private UUID courseId;
    private String title;
    private String description;
    private Integer orderIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CurriculumResponse> curricula;
}
