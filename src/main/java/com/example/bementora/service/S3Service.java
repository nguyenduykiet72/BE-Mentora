package com.example.bementora.service;

import com.example.bementora.dto.response.PresignedUrlResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface S3Service {

     PresignedUrlResult generatePresignedUploadUrl(String fileName, UUID instructorId, UUID courseId);

     void confirmVideoUpload(String s3Key, UUID instructorId, UUID courseId);

     String generatePresignedDownloadUrl(String s3Key);

     String uploadVideo(MultipartFile file, UUID instructorId, UUID courseId);

     void deleteVideo(String s3Key);

     String generateUniqueFileName(String fileName);

     boolean isValidVideoType(String contentType);

     String buildS3Key(UUID instructorId, UUID courseId, String fileName);
}
