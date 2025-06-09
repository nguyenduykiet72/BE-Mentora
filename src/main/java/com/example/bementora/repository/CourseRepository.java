package com.example.bementora.repository;

import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.enums.ApproveEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository  extends JpaRepository<CoursesEntity, UUID> {
    // Newest courses - order by created date DESC, then by rating DESC
    @Query("SELECT c FROM CoursesEntity c WHERE c.approved = :approved ORDER BY c.createdAt desc, c.rating desc nulls last ")
    List<CoursesEntity> findNewestCourses(@Param("approved")ApproveEnum approveEnum, Pageable pageable);

    // Popular courses - order by rating DESC, then by enrollment count
    @Query("SELECT c FROM CoursesEntity c WHERE c.approved = :approved ORDER BY c.rating desc nulls last, c.isBestSeller desc , c.createdAt desc ")
    List<CoursesEntity> findPopularCourses(@Param("approved")ApproveEnum approveEnum, Pageable pageable);

    @Query("SELECT count(c) FROM CoursesEntity c WHERE c.approved = :approved and (c.rating > 0 or c.isBestSeller = true)")
    long countPopularCourses(@Param("approved")ApproveEnum approveEnum);

    // Recommended courses - courses marked as recommended, ordered by rating
    @Query("SELECT c FROM CoursesEntity c WHERE c.approved = :approved and c.isRecommended = true ORDER BY c.rating desc nulls last, c.createdAt desc")
    List<CoursesEntity> findRecommendedCourses(@Param("approved")ApproveEnum approveEnum, Pageable pageable);

    long countByApprovedAndIsRecommended(ApproveEnum approved, Boolean isRecommended);

    long countByApproved(ApproveEnum approved);

    @Query("SELECT c FROM CoursesEntity c WHERE c.approved = :approved AND (c.rating >= :minRating OR c.isBestSeller = true) ORDER BY c.rating DESC NULLS LAST, c.isBestSeller DESC, c.createdAt DESC")
    List<CoursesEntity> findPopularCoursesWithMinRating(@Param("approved") ApproveEnum approved, @Param("minRating") double minRating, Pageable pageable);

}
