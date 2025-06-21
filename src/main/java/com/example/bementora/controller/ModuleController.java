package com.example.bementora.controller;

import com.example.bementora.dto.request.ModuleCreationRequest;
import com.example.bementora.dto.response.ModuleResponse;
import com.example.bementora.service.ModuleService;
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
@RequestMapping("/instructor/modules")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('INSTRUCTOR')")
public class ModuleController {
    private final ModuleService moduleService;
    private final InstructorUtil instructorUtil;

    @PostMapping
    public ResponseEntity<ModuleResponse> createModule(
            @Valid @RequestBody ModuleCreationRequest request,
            Authentication authentication
            ) {
        log.info("Received request to create module");

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        ModuleResponse response = moduleService.createModule(request, instructorId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleResponse>> getModulesByCourse(
            @PathVariable("courseId") UUID courseId,
            Authentication authentication
    ) {
        log.info("Received request to get modules by course id: {}", courseId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        List<ModuleResponse> responses = moduleService.getModulesByCourse(courseId, instructorId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> getModule(
            @PathVariable UUID moduleId,
            Authentication authentication) {

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        ModuleResponse module = moduleService.getModuleById(moduleId, instructorId);

        return ResponseEntity.ok(module);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> updateModule(
            @PathVariable("moduleId") UUID moduleId,
            @Valid @RequestBody ModuleCreationRequest request,
            Authentication authentication
    ) {
        log.info("Received request to update module by id: {}", moduleId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);

        ModuleResponse response = moduleService.updateModule(moduleId, request, instructorId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<String> deleteModule(
            @PathVariable UUID moduleId,
            Authentication authentication) {

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        moduleService.deleteModule(moduleId, instructorId);
        return ResponseEntity.ok("Module deleted successfully");
    }

    @PutMapping("/course/{courseId}/reorder")
    public ResponseEntity<List<ModuleResponse>> reorderModules(
            @PathVariable UUID courseId,
            @RequestBody List<UUID> moduleIds,
            Authentication authentication) {

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        List<ModuleResponse> reorderedModules = moduleService.reorderModules(courseId, moduleIds, instructorId);
        return ResponseEntity.ok(reorderedModules);
    }
}
