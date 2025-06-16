package com.example.bementora.controller;

import com.example.bementora.config.S3ConfigProperties;
import com.example.bementora.dto.request.ConfirmUploadRequest;
import com.example.bementora.dto.request.UploadUrlRequest;
import com.example.bementora.dto.response.PresignedUrlResult;
import com.example.bementora.dto.response.UploadUrlResponse;
import com.example.bementora.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/instructor/videos")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {
    private final S3Service s3Service;
    private final S3Client s3Client;
    private final S3ConfigProperties s3ConfigProperties;

    @PostMapping("/upload-url")
    public ResponseEntity<UploadUrlResponse> getUploadUrl(
            @RequestBody UploadUrlRequest request) {
        // validate file type
        if (!s3Service.isValidVideoType(request.contentType())) {
            return ResponseEntity.badRequest().body(new UploadUrlResponse("Invalid file type"));
        }
        PresignedUrlResult result = s3Service.generatePresignedUploadUrl(
                request.fileName(),
                request.instructorId(),
                request.courseId()
        );

        return ResponseEntity.ok(new UploadUrlResponse(result.uploadUrl(), result.s3Key()));
    }

    @PostMapping("/confirm-upload")
    public ResponseEntity<String> confirmUpload(@RequestBody ConfirmUploadRequest request) {
        try {
            s3Service.confirmVideoUpload(
                    request.s3Key(), request.instructorId(), request.courseId()
            );
            return ResponseEntity.ok("Upload confirmed successfully");
        } catch (Exception e) {
            log.error("Confirm upload failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download-url")
    public ResponseEntity<String> getDownloadUrl(@RequestParam String s3Key) {
        try {
            String downloadUrl = s3Service.generatePresignedDownloadUrl(s3Key);
            return ResponseEntity.ok(downloadUrl);
        } catch (Exception e) {
            log.error("Error generating download URL: ", e);
            return ResponseEntity.badRequest().body("Failed to generate download URL");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteVideo(@RequestParam String s3Key) {
        try {
            s3Service.deleteVideo(s3Key);
            return ResponseEntity.ok("Video deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting video: ", e);
            return ResponseEntity.badRequest().body("Failed to delete video");
        }
    }

}
