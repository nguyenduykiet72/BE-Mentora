package com.example.bementora.entity;

import com.example.bementora.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEntity {
    @Id
    @Column(name = "favoriteId", columnDefinition = "UUID")
    private UUID favoriteId;

    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private CoursesEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}
