package com.example.bementora.service;

import com.example.bementora.dto.response.HomepageResponse;

import java.util.UUID;

public interface HomepageService {
    HomepageResponse getHomePageCourses(
            UUID userId,
            boolean includeNewest,
            boolean includePopular,
            boolean includeRecommended,
            int pageSize
    );
}
