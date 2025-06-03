package com.example.bementora.service;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseCreationResponse;

import java.util.UUID;

public interface CourseService {
    CourseCreationResponse createCourses(CourseCreationRequest courseCreationRequest);

    CourseCreationResponse updateCourse(CourseUpdateRequest courseUpdateRequest, UUID instructorId);
}
