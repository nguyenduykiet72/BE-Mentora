package com.example.bementora.controller;

import com.example.bementora.dto.request.CurriculumCreationRequest;
import com.example.bementora.dto.response.CurriculumResponse;
import com.example.bementora.entity.CurriculumEntity;
import com.example.bementora.service.CurriculumService;
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
@RequestMapping("/instructor/curriculum")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('INSTRUCTOR')")
public class CurriculumController {
    private final CurriculumService curriculumService;
    private final InstructorUtil instructorUtil;

    @PostMapping
    public ResponseEntity<CurriculumResponse> createCurriculum(
            @RequestBody @Valid CurriculumCreationRequest request,
            Authentication authentication
            ) {
        log.info("Received request to create curriculum");

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        CurriculumResponse response = curriculumService.createCurriculum(request, instructorId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<CurriculumResponse>> getCurriculumByModuleId(
            @PathVariable("moduleId") UUID moduleId,
            Authentication authentication
    ) {
        log.info("Received request to get curriculum by module id: {}", moduleId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        List<CurriculumResponse> responses = curriculumService.getCurriculumsByModule(moduleId, instructorId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{curriculumId}")
    public ResponseEntity<CurriculumResponse> getCurriculum(
            @PathVariable UUID curriculumId,
            Authentication authentication) {

        log.info("Received request to get curriculum: {}", curriculumId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        CurriculumResponse curriculum = curriculumService.getCurriculumById(curriculumId, instructorId);

        return ResponseEntity.ok(curriculum);
    }

    @PutMapping("/{curriculumId}")
    public ResponseEntity<CurriculumResponse> updateCurriculum(
            @PathVariable UUID curriculumId,
            @Valid @RequestBody CurriculumCreationRequest request,
            Authentication authentication) {

        log.info("Received request to update curriculum: {}", curriculumId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        CurriculumResponse response = curriculumService.updateCurriculum(curriculumId, request, instructorId);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{curriculumId}")
    public ResponseEntity<String> deleteCurriculum(
            @PathVariable UUID curriculumId,
            Authentication authentication) {

        log.info("Received request to delete curriculum: {}", curriculumId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        curriculumService.deleteCurriculum(curriculumId, instructorId);

        return ResponseEntity.ok("Curriculum deleted successfully");
    }
}
