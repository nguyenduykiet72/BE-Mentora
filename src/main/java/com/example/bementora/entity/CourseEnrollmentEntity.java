package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_course_enrollments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentEntity {
    @Id
    @Column(name = "courseEnrollmentId", columnDefinition = "UUID")
    private UUID courseEnrollmentId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "enrolledAt")
    private LocalDateTime enrolledAt;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
