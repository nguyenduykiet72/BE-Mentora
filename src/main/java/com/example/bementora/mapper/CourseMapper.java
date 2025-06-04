package com.example.bementora.mapper;

import com.example.bementora.dto.request.CourseUpdateRequest;
import com.example.bementora.dto.response.CourseResponse;
import com.example.bementora.entity.CoursesEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "instructorId", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isBestSeller", ignore = true)
    @Mapping(target = "isRecommended", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "courseCategories", ignore = true)
    @Mapping(target = "courseEnrollments", ignore = true)
    @Mapping(target = "learningObjectives", ignore = true)
    @Mapping(target = "requirements", ignore = true)
    @Mapping(target = "courseReviews", ignore = true)
    @Mapping(target = "targetAudience", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "orderDetailEntities", ignore = true)
    @Mapping(target = "voucherCourses", ignore = true)
    void updateCourseFromDto(CourseUpdateRequest dto, @MappingTarget CoursesEntity entity);
    
    CourseResponse entityToResponse(CoursesEntity entity);
}