package com.example.bementora.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_course_requirements")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequirementEntity {
    @Id
    @Column(name = "requirementId", columnDefinition = "UUID")
    private UUID requirementId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "orderIndex")
    private Integer orderIndex;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;
}
