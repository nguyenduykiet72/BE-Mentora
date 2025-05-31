package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tbl_course_categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategoryEntity {
    @Id
    @Column(name = "courseCategoryId", columnDefinition = "UUID")
    private UUID courseCategoryId;

    @Column(name = "categoryId", columnDefinition = "UUID")
    private UUID categoryId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;
}
