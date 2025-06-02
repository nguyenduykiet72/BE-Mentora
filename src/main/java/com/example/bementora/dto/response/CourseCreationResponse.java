package com.example.bementora.dto.response;

import com.example.bementora.enums.ApproveEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourseCreationResponse {
    private UUID instructorId;
    private String title;
    private String description;
    private String overview;
    private Integer durationTime;
    private BigDecimal price;
    private ApproveEnum approved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String thumbnail;
}
