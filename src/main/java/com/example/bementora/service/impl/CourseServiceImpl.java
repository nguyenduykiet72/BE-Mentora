package com.example.bementora.service.impl;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseCreationResponse;
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
    public CourseCreationResponse createCourses(CourseCreationRequest courseCreationRequest) {
        CoursesEntity coursesEntity = new CoursesEntity();

        coursesEntity.setInstructorId(courseCreationRequest.getInstructorId());
        coursesEntity.setTitle(courseCreationRequest.getTitle());
        coursesEntity.setDescription(courseCreationRequest.getDescription());
        coursesEntity.setOverview(courseCreationRequest.getOverview());
        coursesEntity.setPrice(courseCreationRequest.getPrice());
        coursesEntity.setDurationTime(courseCreationRequest.getDurationTime());
        coursesEntity.setThumbnail(courseCreationRequest.getThumbnail());
        coursesEntity.setApproved(courseCreationRequest.getApproved());

        LocalDateTime now = LocalDateTime.now();
        coursesEntity.setCreatedAt(now);
        coursesEntity.setUpdatedAt(now);

        CoursesEntity savedCourse =  courseRepository.save(coursesEntity);

        CourseCreationResponse response = new CourseCreationResponse();
        response.setInstructorId(savedCourse.getInstructorId());
        response.setTitle(savedCourse.getTitle());
        response.setDescription(savedCourse.getDescription());
        response.setOverview(savedCourse.getOverview());
        response.setDurationTime(savedCourse.getDurationTime());
        response.setPrice(savedCourse.getPrice());
        response.setApproved(savedCourse.getApproved());
        response.setThumbnail(savedCourse.getThumbnail());
        response.setCreatedAt(savedCourse.getCreatedAt());
        response.setUpdatedAt(savedCourse.getUpdatedAt());

        return response;
    }

    @Override
    @Transactional
    public CourseCreationResponse updateCourse(CourseUpdateRequest courseUpdateRequest, UUID instructorId) {
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
