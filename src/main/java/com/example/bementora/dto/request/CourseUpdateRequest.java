package com.example.bementora.dto.request;

import com.example.bementora.enums.ApproveEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CourseUpdateRequest {
    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Title is required")
    @Min(value = 3, message = "Title must be at least 3 characters long")
    private String title;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Overview is required")
    private String overview;

    @Min(value = 1, message = "Duration time must be at least 1 minute")
    private Integer durationTime;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    private ApproveEnum approved;

    private String thumbnail;
}
