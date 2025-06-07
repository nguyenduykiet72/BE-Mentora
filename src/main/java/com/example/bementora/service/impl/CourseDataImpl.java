package com.example.bementora.service.impl;

import com.example.bementora.dto.response.CourseResponse;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.enums.ApproveEnum;
import com.example.bementora.mapper.CourseMapper;
import com.example.bementora.repository.CourseRepository;
import com.example.bementora.service.CourseDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDataImpl implements CourseDataService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Cacheable(value = "homepage-newest", unless = "#result.isEmpty()")
    public List<CourseResponse> getNewestCourses(int pageSize) {
        log.info("Fetching {} newest courses from database", pageSize);
        log.info("📢 CACHE MISS: Fetching {} newest courses from database", pageSize);
        List<CoursesEntity> courses = courseRepository.findNewestCourses(
                ApproveEnum.APPROVED,
                PageRequest.of(0, pageSize)
        );
        return courseMapper.entityToResponse(courses);
    }

    @Override
    @Cacheable(value = "homepage-popular", unless = "#result.isEmpty()")
    public List<CourseResponse> getPopularCourses(int pageSize) {
        log.info("Fetching {} popular courses from database", pageSize);
        List<CoursesEntity> courses = courseRepository.findPopularCourses(
                ApproveEnum.APPROVED,
                PageRequest.of(0, pageSize)
        );
        return courseMapper.entityToResponse(courses);
    }

    @Override
    @Cacheable(value = "homepage-recommended", unless = "#result.isEmpty()")
    public List<CourseResponse> getRecommendedCourses(int pageSize) {
        log.info("Fetching {} recommended courses from database", pageSize);
        List<CoursesEntity> courses = courseRepository.findRecommendedCourses(
                ApproveEnum.APPROVED,
                PageRequest.of(0, pageSize)
        );
        return courseMapper.entityToResponse(courses);
    }
}
