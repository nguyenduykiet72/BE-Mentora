package com.example.bementora.repository;

import com.example.bementora.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, UUID> {
    @Query("SELECT m FROM ModuleEntity m JOIN FETCH m.course c WHERE c.instructorId = :instructorId AND m.moduleId = :moduleId")
    Optional<ModuleEntity> findByIdAndInstructorId(@Param("moduleId") UUID moduleId, @Param("instructorId") UUID instructorId);

    @Query("SELECT m FROM ModuleEntity m JOIN FETCH m.course c WHERE c.instructorId = :instructorId AND m.courseId = :courseId ORDER BY m.orderIndex ASC")
    List<ModuleEntity> findByCourseIdAndInstructorId(@Param("courseId") UUID courseId, @Param("instructorId") UUID instructorId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM ModuleEntity m JOIN m.course c WHERE c.instructorId = :instructorId AND m.courseId = :courseId AND m.orderIndex = :orderIndex AND (:moduleId IS NULL OR m.moduleId != :moduleId)")
    boolean existsByOrderIndexInCourse(@Param("courseId") UUID courseId, @Param("orderIndex") Integer orderIndex, @Param("instructorId") UUID instructorId, @Param("moduleId") UUID moduleId);

    @Query("SELECT COUNT(m) FROM ModuleEntity m JOIN m.course c WHERE c.instructorId = :instructorId AND m.courseId = :courseId")
    long countByCourseIdAndInstructorId(@Param("courseId") UUID courseId, @Param("instructorId") UUID instructorId);
}
