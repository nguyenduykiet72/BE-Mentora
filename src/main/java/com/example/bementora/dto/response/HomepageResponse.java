package com.example.bementora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageResponse {
    private List<CourseResponse> newestCourses;
    private  List<CourseResponse> popularCourses;
    private List<CourseResponse> recommendedCourses;
    private HomepageMetadata metadata;
}
