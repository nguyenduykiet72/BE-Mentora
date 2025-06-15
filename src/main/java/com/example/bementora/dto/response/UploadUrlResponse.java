package com.example.bementora.dto.response;

public record UploadUrlResponse(String uploadUrl, String s3Key, String message) {
    public UploadUrlResponse(String message) {
        this(null, null, message);
    }

    public UploadUrlResponse(String uploadUrl, String s3Key) {
        this(uploadUrl, s3Key, "Success");
    }
}