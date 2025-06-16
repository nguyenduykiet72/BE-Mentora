package com.example.bementora.service.impl;

import com.example.bementora.config.S3ConfigProperties;
import com.example.bementora.config.VideoConfigProperties;
import com.example.bementora.dto.response.PresignedUrlResult;
import com.example.bementora.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3ConfigProperties s3ConfigProperties;
    private final VideoConfigProperties videoConfigProperties;

    @Override
    public PresignedUrlResult generatePresignedUploadUrl(String fileName, UUID instructorId, UUID courseId) {
        try {
            String uniqueFileName = generateUniqueFileName(fileName);
            String s3Key = buildS3Key(instructorId, courseId, uniqueFileName);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(s3Key)
                    .contentType("video/mp4")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(s3ConfigProperties.getPresignedUrlExpiration()))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            log.info("Generated presigned upload URL for key: {}", s3Key);

            return new PresignedUrlResult(presignedRequest.url().toString(), s3Key);

        } catch (Exception e) {
            log.error("Error generating presigned upload URL: ", e);
            throw new RuntimeException("Failed to generate upload URL", e);
        }
    }

    @Override
    public void confirmVideoUpload(String s3Key, UUID instructorId, UUID courseId) {
        try {
            //verify object exists
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(s3Key)
                    .build();

            HeadObjectResponse response = s3Client.headObject(headObjectRequest);

            log.info("Video upload confirmed for key: {}, size: {} bytes",
                    s3Key, response.contentLength());

            // TODO: Save video metadata to database


        } catch (NoSuchKeyException e) {
            log.error("Video not found in S3: {}", s3Key);
            throw new RuntimeException("Video upload was not completed", e);
        } catch (Exception e) {
            log.error("Error confirming video upload: ", e);
            throw new RuntimeException("Failed to confirm video upload", e);
        }
    }

    @Override
    public String generatePresignedDownloadUrl(String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(s3Key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(s3ConfigProperties.getPresignedUrlExpiration()))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("Error generating presigned download URL: ", e);
            throw new RuntimeException("Failed to generate download URL", e);
        }
    }

    @Override
    public String uploadVideo(MultipartFile file, UUID instructorId, UUID courseId) {
        try {
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
            String s3Key = buildS3Key(instructorId, courseId, uniqueFileName);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            file.getInputStream(), file.getSize()));

            log.info("Video uploaded directly to S3: {}", s3Key);
            return s3Key;

        } catch (Exception e) {
            log.error("Error uploading video directly: ", e);
            throw new RuntimeException("Failed to upload video", e);
        }
    }

    @Override
    public void deleteVideo(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("Video deleted from S3: {}", s3Key);

        } catch (Exception e) {
            log.error("Error deleting video: ", e);
            throw new RuntimeException("Failed to delete video", e);
        }
    }

    @Override
    public String generateUniqueFileName(String fileName) {
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
    }

    @Override
    public boolean isValidVideoType(String contentType) {
        return contentType != null &&
                videoConfigProperties.getAllowedTypes().contains(contentType.toLowerCase());
    }

    @Override
    public String buildS3Key(UUID instructorId, UUID courseId, String fileName) {
        return String.format("%s/instructor-%s/course-%s/%s",
                videoConfigProperties.getStoragePath(),
                instructorId,
                courseId,
                fileName
        );
    }
}
