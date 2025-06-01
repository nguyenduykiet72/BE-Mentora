package com.example.bementora.dto.request;

import com.example.bementora.enums.RoleEnum;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserCreationRequest implements Serializable {
    @Email()
    @NotBlank(message = "Email cannot blank")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String avatar;

    @NotNull(message = "Role cannot blank")
    private RoleEnum role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull(message = "Full name cannot blank")
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
