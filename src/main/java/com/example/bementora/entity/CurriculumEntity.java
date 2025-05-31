package com.example.bementora.entity;

import com.example.bementora.enums.CurriculumEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_curricula")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumEntity {
    @Id
    @Column(name = "curriculumId", columnDefinition = "UUID")
    private UUID curriculumId;

    @Column(name = "moduleId", columnDefinition = "UUID")
    private UUID moduleId;

    @Column(name = "title")
    private String title;

    @Column(name = "orderIndex")
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CurriculumEnum type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moduleId", insertable = false, updatable = false)
    private ModuleEntity module;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<CurriculumProgressEntity> curriculumProgress;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<DiscussingEntity> discussions;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<LectureEntity> lectures;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<QuizEntity> quizzes;
}
