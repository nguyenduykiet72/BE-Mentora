package com.example.bementora.repository;

import com.example.bementora.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, UUID> {
    Optional<VoucherEntity> findByCodeAndIsActiveTrue(String code);

    @Query("SELECT v FROM VoucherEntity v WHERE v.code = :code AND v.isActive = true " +
            "AND v.startDate <= :now AND v.endDate >= :now " +
            "AND (v.maxUsage IS NULL OR v.maxUsage > 0)")
    Optional<VoucherEntity> findValidVoucherByCode(@Param("code") String code, @Param("now") LocalDateTime now);

    @Query("SELECT v FROM VoucherEntity v LEFT JOIN VoucherCourseEntity vc ON v.voucherId = vc.voucherId " +
            "WHERE v.code = :code AND v.isActive = true " +
            "AND v.startDate <= :now AND v.endDate >= :now " +
            "AND (v.maxUsage IS NULL OR v.maxUsage > 0) " +
            "AND (v.scope = 'ALL_COURSES' OR " +
            "     (v.scope = 'SPECIFIC_COURSES' AND vc.courseId = :courseId) OR " +
            "     (v.scope = 'CATEGORY' AND EXISTS (SELECT cc FROM CourseCategoryEntity cc WHERE cc.courseId = :courseId AND cc.categoryId = v.categoryId)))")
    Optional<VoucherEntity> findValidVoucherForCourse(@Param("code") String code,
                                                      @Param("courseId") UUID courseId,
                                                      @Param("now") LocalDateTime now);
}
