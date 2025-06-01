package com.example.bementora.service.impl;

import com.example.bementora.dto.request.UserCreationRequest;
import com.example.bementora.dto.response.UserCreationResponse;
import com.example.bementora.dto.response.UserResponse;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.exception.EmailAlreadyExistsException;
import com.example.bementora.repository.UserRepository;
import com.example.bementora.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImplService implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse findById(UUID userId) {
        log.info("Searching for user with ID: {}", userId);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Found user entity: {}", userEntity.getUserId());

        return UserResponse.builder()
                .id(userEntity.getUserId())
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .avatar(userEntity.getAvatar())
                .build();
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserCreationResponse createUser(UserCreationRequest userCreationRequest) {
        log.info("Creating user with request: {}", userCreationRequest);

        if (userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + userCreationRequest.getEmail() + " is already taken. Please try another email.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setEmail(userCreationRequest.getEmail());
        userEntity.setFullName(userCreationRequest.getFullName());
        userEntity.setAvatar(userCreationRequest.getAvatar());
        userEntity.setPassword(userCreationRequest.getPassword());
        userEntity.setRole(userCreationRequest.getRole());
        userEntity.setFacebookLink(userCreationRequest.getFacebookLink());
        userEntity.setLinkedinLink(userCreationRequest.getLinkedinLink());
        userEntity.setWebsiteLink(userCreationRequest.getWebsiteLink());
        userEntity.setYoutubeLink(userCreationRequest.getYoutubeLink());
        userEntity.setDescription(userCreationRequest.getDescription());
        userEntity.setTitle(userCreationRequest.getTitle());
        userEntity.setIsEmailVerified(false);

        LocalDateTime now = LocalDateTime.now();
        userEntity.setCreatedAt(now);
        userEntity.setUpdatedAt(now);

        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User created successfully with ID: {}", savedUser.getUserId());

        UserCreationResponse response = new UserCreationResponse();
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());
        response.setAvatar(savedUser.getAvatar());
        response.setRole(savedUser.getRole());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        response.setFacebookLink(savedUser.getFacebookLink());
        response.setLinkedinLink(savedUser.getLinkedinLink());
        response.setWebsiteLink(savedUser.getWebsiteLink());
        response.setYoutubeLink(savedUser.getYoutubeLink());
        response.setDescription(savedUser.getDescription());
        response.setTitle(savedUser.getTitle());
        response.setIsEmailVerified(savedUser.getIsEmailVerified());



        return response;
    }


}
