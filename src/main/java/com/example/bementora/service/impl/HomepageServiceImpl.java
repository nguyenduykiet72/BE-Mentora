package com.example.bementora.service.impl;

import com.example.bementora.dto.response.CourseResponse;
import com.example.bementora.dto.response.HomepageMetadata;
import com.example.bementora.dto.response.HomepageResponse;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.enums.ApproveEnum;
import com.example.bementora.mapper.CourseMapper;
import com.example.bementora.repository.CourseRepository;
import com.example.bementora.service.CourseDataService;
import com.example.bementora.service.HomepageService;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomepageServiceImpl implements HomepageService {
    private final CourseRepository courseRepository;
    private final CourseDataService courseData;

    @Override
    public HomepageResponse getHomePageCourses(UUID userId, boolean includeNewest, boolean includePopular, boolean includeRecommended, int pageSize) {
        log.info("Getting homepage courses for user: {}, pageSize: {}", userId, pageSize);

        List<CourseResponse> newestCourses = new ArrayList<>();
        List<CourseResponse> popularCourses = new ArrayList<>();
        List<CourseResponse> recommendedCourses = new ArrayList<>();

        // Get newest courses (by created date + rating)
        if (includeNewest) {
            newestCourses = courseData.getNewestCourses(pageSize);
        }

        // Get popular courses (by rating + best seller status)
        if (includePopular) {
            popularCourses = courseData.getPopularCourses(pageSize);
        }

        // Get recommended courses
        if (includeRecommended) {
            recommendedCourses = courseData.getRecommendedCourses(pageSize);
        }

        HomepageMetadata metadata = createMetadata(
                pageSize,
                newestCourses.size(),
                popularCourses.size(),
                recommendedCourses.size()
        );

        return HomepageResponse.builder()
                .newestCourses(newestCourses)
                .popularCourses(popularCourses)
                .recommendedCourses(recommendedCourses)
                .metadata(metadata)
                .build();
    }

    private HomepageMetadata createMetadata(int pageSize, int newestCount, int popularCount, int recommendedCount) {
        long totalNewest = courseRepository.countByApproved(ApproveEnum.APPROVED);
        long totalPopular = courseRepository.countPopularCourses(ApproveEnum.APPROVED);
        long totalRecommended = courseRepository.countByApprovedAndIsRecommended(ApproveEnum.APPROVED, true);

        return HomepageMetadata.builder()
                .hasNewCourse(newestCount >= pageSize && totalNewest > pageSize)
                .hasPopularCourse(popularCount >= pageSize && totalPopular > pageSize)
                .hasRecommendedCourse(recommendedCount >= pageSize && totalRecommended > pageSize)
                .totalNewCourse((int) totalNewest)
                .totalPopularCourse((int) totalPopular)
                .totalRecommendedCourse((int) totalRecommended)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

}
