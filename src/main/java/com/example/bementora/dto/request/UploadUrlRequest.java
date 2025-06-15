package com.example.bementora.dto.request;

public record UploadUrlRequest(String fileName, String contentType, Long instructorId, Long courseId) {}

