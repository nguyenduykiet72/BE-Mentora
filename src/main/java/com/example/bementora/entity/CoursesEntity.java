package com.example.bementora.entity;

import com.example.bementora.enums.ApproveEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_courses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoursesEntity {
    @Id
    @Column(name = "courseId", columnDefinition = "UUID")
    private UUID courseId;

    @Column(name = "instructorId", columnDefinition = "UUID")
    private UUID instructorId;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "durationTime")
    private Integer durationTime;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "approved")
    private ApproveEnum approved;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "isBestSeller")
    private Boolean isBestSeller = false;

    @Column(name = "isRecommended")
    private Boolean isRecommended = false;

    @Column(name = "thumbnail")
    private String thumbnail;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructorId", insertable = false, updatable = false)
    private InstructorEntity instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseCategoryEntity> courseCategories;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseEnrollmentEntity> courseEnrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseLearningObjectiveEntity> learningObjectives;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseRequirementEntity> requirements;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseReviewEntity> courseReviews;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseTargetAudienceEntity> targetAudience;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<ModuleEntity> modules;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<OrderDetailEntity> orderDetailEntities;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<VoucherCourseEntity> voucherCourses;
}
