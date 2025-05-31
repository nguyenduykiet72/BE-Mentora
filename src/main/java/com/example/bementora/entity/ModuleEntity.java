package com.example.bementora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleEntity {
    @Id
    @Column(name = "moduleId", columnDefinition = "UUID")
    private UUID moduleId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "title")
    private String title;

    @Column(name = "orderIndex")
    private Integer orderIndex;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<CurriculumEntity> curricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;
}
