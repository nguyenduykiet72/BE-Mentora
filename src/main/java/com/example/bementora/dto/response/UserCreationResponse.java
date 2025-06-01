package com.example.bementora.dto.response;

import com.example.bementora.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserCreationResponse implements Serializable {
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
