package com.example.bementora.repository;

import com.example.bementora.entity.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository  extends JpaRepository<CoursesEntity, UUID> {
}
