package com.example.bementora.entity;

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
@Table(name = "tbl_instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "instructorId", columnDefinition = "UUID")
    private UUID instructorId;
    
    @Column(name = "userId", columnDefinition = "UUID")
    private UUID userId;
    
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "profilePicture")
    private String profilePicture;
    
    @Column(name = "experience", columnDefinition = "TEXT")
    private String experience;
    
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;
    
    @Column(name = "isVerified")
    private Boolean isVerified;
    
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    
    @Column(name = "instructorName")
    private String instructorName;
    
    @Column(name = "paypalEmail")
    private String paypalEmail;
    
    @Column(name = "isPaypalVerified")
    private Boolean isPaypalVerified = false;
    
    @Column(name = "paypalVerificationToken")
    private String paypalVerificationToken;
    
    @Column(name = "paypalVerificationTokenExp")
    private LocalDateTime paypalVerificationTokenExp;
    
    // Relationships
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<CoursesEntity> courses;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;
}