package com.example.bementora.dto.request;

public record ConfirmUploadRequest(String s3Key, Long instructorId, Long courseId) {}