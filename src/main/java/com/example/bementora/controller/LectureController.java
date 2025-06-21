package com.example.bementora.controller;

import com.example.bementora.dto.request.LectureCreationRequest;
import com.example.bementora.dto.response.LectureResponse;
import com.example.bementora.service.LectureService;
import com.example.bementora.util.InstructorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instructor/lecture")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('INSTRUCTOR')")
public class LectureController {
    private final LectureService lectureService;
    private final InstructorUtil instructorUtil;

    @PostMapping
    public ResponseEntity<LectureResponse> createLecture(
            @Valid @RequestBody LectureCreationRequest request,
            Authentication authentication
            ) {
        log.info("Received request to create lecture for curriculum: {}", request.getCurriculumId());

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        LectureResponse response = lectureService.createLecture(request, instructorId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/curriculum/{curriculumId}")
    public ResponseEntity<List<LectureResponse>> getLecturesByCurriculum(
            @PathVariable UUID curriculumId,
            Authentication authentication) {

        log.info("Received request to get lectures for curriculum: {}", curriculumId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        List<LectureResponse> responses = lectureService.getLecturesByCurriculum(curriculumId, instructorId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureResponse> getLecture(
            @PathVariable UUID lectureId,
            Authentication authentication) {

        log.info("Received request to get lecture: {}", lectureId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        LectureResponse lecture = lectureService.getLectureById(lectureId, instructorId);

        return ResponseEntity.ok(lecture);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<LectureResponse> updateLecture(
            @PathVariable UUID lectureId,
            @Valid @RequestBody LectureCreationRequest request,
            Authentication authentication) {

        log.info("Received request to update lecture: {}", lectureId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        LectureResponse response = lectureService.updateLecture(lectureId, request, instructorId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lectureId}")
    public ResponseEntity<String> deleteLecture(
            @PathVariable UUID lectureId,
            Authentication authentication) {

        log.info("Received request to delete lecture: {}", lectureId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        lectureService.deleteLecture(lectureId, instructorId);

        return ResponseEntity.ok("Lecture deleted successfully");
    }

    @PutMapping("/{lectureId}/attach-video")
    public ResponseEntity<LectureResponse> attachVideo(
            @PathVariable UUID lectureId,
            @RequestParam String s3Key,
            Authentication authentication) {
        log.info("Received request to attach video {} to lecture: {}", s3Key, lectureId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        LectureResponse response = lectureService.attachVideoToLecture(lectureId,s3Key,instructorId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lectureId}/video-url")
    public ResponseEntity<String> generateVideoViewUrl(
            @PathVariable UUID lectureId,
            Authentication authentication) {

        log.info("Received request to generate video view URL for lecture: {}", lectureId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        String videoUrl = lectureService.generateVideoViewUrl(lectureId, instructorId);

        return ResponseEntity.ok(videoUrl);
    }
}
