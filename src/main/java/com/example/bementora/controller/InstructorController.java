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
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        log.info("Confirm upload request: {}", request);
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

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listVideos(
            @RequestParam UUID instructorId,
            @RequestParam(required = false) UUID courseId) {

        try {
            String prefix = courseId != null ?
                    String.format("videos/instructor-%s/course-%s/", instructorId, courseId) :
                    String.format("videos/instructor-%s/", instructorId);

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

            List<Map<String, Object>> videos = response.contents().stream()
                    .map(obj -> {
                        Map<String, Object> video = new HashMap<>();
                        video.put("s3Key", obj.key());
                        video.put("fileName", obj.key().substring(obj.key().lastIndexOf("/") + 1));
                        video.put("size", obj.size());
                        video.put("lastModified", obj.lastModified());
                        video.put("downloadUrl", s3Service.generatePresignedDownloadUrl(obj.key()));
                        return video;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(videos);

        } catch (Exception e) {
            log.error("Error listing videos: ", e);
            return ResponseEntity.badRequest().build();

        }
    }
}