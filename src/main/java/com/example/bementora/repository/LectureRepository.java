package com.example.bementora.repository;

import com.example.bementora.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<LectureEntity, Long> {
    @Query("SELECT l FROM LectureEntity l JOIN FETCH l.curriculum c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND l.lectureId = :lectureId")
    Optional<LectureEntity> findByIdAndInstructorId(@Param("lectureId") UUID lectureId, @Param("instructorId") UUID instructorId);

    @Query("SELECT l FROM LectureEntity l JOIN FETCH l.curriculum c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND l.curriculumId = :curriculumId")
    List<LectureEntity> findByCurriculumIdAndInstructorId(@Param("curriculumId") UUID curriculumId, @Param("instructorId") UUID instructorId);
}
