package com.example.bementora.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

     String generatePresignedUploadUrl(String fileName, Long instructorId, Long courseId);

     void confirmVideoUpload(String s3Key, Long instructorId, Long courseId);

     String generatePresignedDownloadUrl(String s3Key);

     String uploadVideo(MultipartFile file, Long instructorId, Long courseId);

     void deleteVideo(String s3Key);

     String generateUniqueFileName(String fileName);

     boolean isValidVideoType(String contentType);

     String buildS3Key(Long instructorId, Long courseId, String fileName);
}
