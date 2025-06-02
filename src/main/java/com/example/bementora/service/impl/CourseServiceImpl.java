package com.example.bementora.service.impl;

import com.example.bementora.dto.request.CourseCreationRequest;
import com.example.bementora.dto.response.CourseCreationResponse;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.repository.CourseRepository;
import com.example.bementora.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;


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
}
