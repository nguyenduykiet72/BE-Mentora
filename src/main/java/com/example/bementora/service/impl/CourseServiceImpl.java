package com.example.bementora.service.impl;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseResponse;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.mapper.CourseMapper;
import com.example.bementora.repository.CourseRepository;
import com.example.bementora.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse createCourses(CourseCreationRequest courseCreationRequest) {

        CoursesEntity coursesEntity = courseMapper.creationRequestToEntity(courseCreationRequest);

        LocalDateTime now = LocalDateTime.now();
        coursesEntity.setCreatedAt(now);
        coursesEntity.setUpdatedAt(now);

        CoursesEntity savedCourse = courseRepository.save(coursesEntity);

        return courseMapper.entityToResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(CourseUpdateRequest courseUpdateRequest, UUID instructorId) {
        log.info("Updating course with request: {} by instructorId {}", courseUpdateRequest, instructorId);

        CoursesEntity coursesEntity = courseRepository.findById(courseUpdateRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseUpdateRequest.getCourseId()));

        if (!coursesEntity.getInstructorId().equals(instructorId)) {
            throw new RuntimeException("Instructor is not authorized to update this course");
        }

        courseMapper.updateCourseFromDto(courseUpdateRequest, coursesEntity);

        coursesEntity.setUpdatedAt(LocalDateTime.now());

        CoursesEntity updatedCourse = courseRepository.save(coursesEntity);

        return courseMapper.entityToResponse(updatedCourse);
    }

}
