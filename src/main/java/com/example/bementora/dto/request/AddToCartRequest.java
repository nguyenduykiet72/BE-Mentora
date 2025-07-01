package com.example.bementora.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddToCartRequest {
    @NotNull(message = "Course ID is required")
    private UUID courseId;
    private UUID userId;
    private String sessionId;
    private String voucherCode;
}
