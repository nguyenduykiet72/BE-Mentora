package com.example.bementora.repository;

import com.example.bementora.entity.CourseEnrollmentEntity;
import com.example.bementora.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollmentEntity, UUID> {
    List<CourseEnrollmentEntity> user(UserEntity user);

    boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);

    List<CourseEnrollmentEntity> findByUserId(UUID userId);
}
