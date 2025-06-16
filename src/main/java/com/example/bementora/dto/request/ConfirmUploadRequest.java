package com.example.bementora.dto.request;

import java.util.UUID;

public record ConfirmUploadRequest(String s3Key, UUID instructorId, UUID courseId) {}