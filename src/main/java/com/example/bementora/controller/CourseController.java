package com.example.bementora.controller;

import com.example.bementora.common.ApiResponse;
import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseResponse;
import com.example.bementora.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CourseCreationRequest courseCreationRequest) {
        log.info("Received request to create course: {}", courseCreationRequest);

        CourseResponse createCourse = courseService.createCourses(courseCreationRequest);

        ApiResponse<CourseResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User created successfully",
                createCourse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest,
                                                                    @RequestParam UUID instructorId) {
        log.info("Received request to update course: {}", courseUpdateRequest);

        CourseResponse updateCourse = courseService.updateCourse(courseUpdateRequest, instructorId);

        ApiResponse<CourseResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User created successfully",
                updateCourse
        );

        return ResponseEntity.ok(response);
    }
}
