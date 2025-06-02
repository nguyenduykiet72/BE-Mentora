package com.example.bementora.service;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.response.CourseCreationResponse;
import com.example.bementora.entity.CoursesEntity;

public interface CourseService {
    CourseCreationResponse createCourses(CourseCreationRequest courseCreationRequest);
}
