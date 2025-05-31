package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_course_reviews")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseReviewEntity {
    @Id
    @Column(name = "reviewId", columnDefinition = "UUID")
    private UUID reviewId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
