package com.example.bementora.dto.response;

import com.example.bementora.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationResponse implements Serializable {
    private UUID id;
    private String email;
    private String password;
    private String avatar;
    private RoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fullName;
    private String facebookLink;
    private String linkedinLink;
    private String websiteLink;
    private String youtubeLink;
    private String description;
    private String title;
    private Boolean isEmailVerified = false;
    private String verificationEmailToken;
    private LocalDateTime verificationEmailTokenExp;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExp;
}
