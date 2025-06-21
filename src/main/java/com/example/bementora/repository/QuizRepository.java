package com.example.bementora.repository;

import com.example.bementora.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, UUID> {
    @Query("SELECT q FROM QuizEntity q JOIN FETCH q.curriculum c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND q.quizId = :quizId")
    Optional<QuizEntity> findByIdAndInstructorId(@Param("quizId") UUID quizId, @Param("instructorId") UUID instructorId);

    @Query("SELECT q FROM QuizEntity q JOIN FETCH q.curriculum c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND q.curriculumId = :curriculumId")
    List<QuizEntity> findByCurriculumIdAndInstructorId(@Param("curriculumId") UUID curriculumId, @Param("instructorId") UUID instructorId);
}
