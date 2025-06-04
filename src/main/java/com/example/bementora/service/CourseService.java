package com.example.bementora.service;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseResponse;

import java.util.UUID;

public interface CourseService {
    CourseResponse createCourses(CourseCreationRequest courseCreationRequest);

    CourseResponse updateCourse(CourseUpdateRequest courseUpdateRequest, UUID instructorId);
}
