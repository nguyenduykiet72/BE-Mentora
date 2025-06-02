package com.example.bementora.dto.request;

import com.example.bementora.enums.ApproveEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourseCreationRequest {
    @NotNull
    private UUID instructorId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String overview;

    @NotNull
    private Integer durationTime;

    @NotNull
    private BigDecimal price;

    private ApproveEnum approved = ApproveEnum.PENDING;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String thumbnail;
}
