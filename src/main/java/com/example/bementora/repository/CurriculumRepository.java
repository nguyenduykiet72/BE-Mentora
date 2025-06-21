package com.example.bementora.repository;

import com.example.bementora.entity.CurriculumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurriculumRepository extends JpaRepository<CurriculumEntity, Long> {
    @Query("SELECT c FROM CurriculumEntity c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND c.curriculumId = :curriculumId")
    Optional<CurriculumEntity> findByIdAndInstructorId(@Param("curriculumId") UUID curriculumId, @Param("instructorId") UUID instructorId);

    @Query("SELECT c FROM CurriculumEntity c JOIN FETCH c.module m JOIN FETCH m.course co WHERE co.instructorId = :instructorId AND c.moduleId = :moduleId ORDER BY c.orderIndex ASC")
    List<CurriculumEntity> findByModuleIdAndInstructorId(@Param("moduleId") UUID moduleId, @Param("instructorId") UUID instructorId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CurriculumEntity c JOIN c.module m JOIN m.course co WHERE co.instructorId = :instructorId AND c.moduleId = :moduleId AND c.orderIndex = :orderIndex AND (:curriculumId IS NULL OR c.curriculumId != :curriculumId)")
    boolean existsByOrderIndexInModule(@Param("moduleId") UUID moduleId, @Param("orderIndex") Integer orderIndex, @Param("instructorId") UUID instructorId, @Param("curriculumId") UUID curriculumId);
}
