package com.example.bementora.entity;

import com.example.bementora.enums.CurriculumEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_disscussing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscussingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "discussingId", columnDefinition = "UUID")
    private UUID discussingId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "curriculumId", columnDefinition = "UUID")
    private UUID curriculumId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
    private CurriculumEntity curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
