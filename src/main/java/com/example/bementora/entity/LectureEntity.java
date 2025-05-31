package com.example.bementora.entity;

import com.example.bementora.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_lectures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureEntity {
    @Id
    @Column(name = "lectureId", columnDefinition = "UUID")
    private UUID lectureId;

    @Column(name = "curriculumId", columnDefinition = "UUID")
    private UUID curriculumId;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "videoUrl")
    private String videoUrl;

    @Column(name = "articleContent", columnDefinition = "TEXT")
    private String articleContent;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "isFree")
    private Boolean isFree = false;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private List<LectureProgressEntity> lectureProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
    private CurriculumEntity curriculum;
}
