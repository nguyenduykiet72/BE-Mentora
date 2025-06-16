package com.example.bementora.dto.request;

import java.util.UUID;

public record UploadUrlRequest(String fileName, String contentType, UUID instructorId, UUID courseId) {}

