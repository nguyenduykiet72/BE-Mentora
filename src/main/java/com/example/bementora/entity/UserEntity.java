package com.example.bementora.entity;

import com.example.bementora.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "facebookLink")
    private String facebookLink;

    @Column(name = "linkedinLink")
    private String linkedinLink;

    @Column(name = "websiteLink")
    private String websiteLink;

    @Column(name = "youtubeLink")
    private String youtubeLink;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "isEmailVerified")
    private Boolean isEmailVerified = false;

    @Column(name = "verificationEmailToken")
    private String verificationEmailToken;

    @Column(name = "verificationEmailTokenExp")
    private LocalDateTime verificationEmailTokenExp;

    @Column(name = "resetPasswordToken")
    private String resetPasswordToken;

    @Column(name = "resetPasswordTokenExp")
    private LocalDateTime resetPasswordTokenExp;

    // Relationships
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<CartEntity> cartEntities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CourseEnrollmentEntity> courseEnrollments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CourseReviewEntity> courseReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CurriculumProgressEntity> curriculumProgress;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DiscussingEntity> discussions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<InstructorEntity> instructorEntities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LectureProgressEntity> lectureProgress;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PaymentEntity> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<QuizAttemptEntity> quizAttempts;

}
