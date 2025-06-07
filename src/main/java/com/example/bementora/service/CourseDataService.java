package com.example.bementora.service;

import com.example.bementora.dto.response.CourseResponse;

import java.util.List;

public interface CourseDataService {
    List<CourseResponse> getNewestCourses(int pageSize);
    List<CourseResponse> getPopularCourses(int pageSize);
    List<CourseResponse> getRecommendedCourses(int pageSize);
}
