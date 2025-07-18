package com.example.bementora.service.impl;

import com.example.bementora.dto.request.UserCreationRequest;
import com.example.bementora.dto.response.UserCreationResponse;
import com.example.bementora.entity.InstructorEntity;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.enums.RoleEnum;
import com.example.bementora.exception.EmailAlreadyExistsException;
import com.example.bementora.mapper.UserMapper;
import com.example.bementora.repository.InstructorRepository;
import com.example.bementora.repository.UserRepository;
import com.example.bementora.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final UserMapper userMapper;

    @Override
    public UserCreationResponse findById(UUID userId) {
        log.info("Searching for user with ID: {}", userId);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Found user entity: {}", userEntity.getUserId());

        return userMapper.entityToResponse(userEntity);
    }

    @Override
    public List<UserCreationResponse> getAllUser() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.entityToResponse(users);
    }

    @Override
    @Transactional // Important to ensure both user and instructor are created in a single transaction
    public UserCreationResponse createUser(UserCreationRequest userCreationRequest) {
        log.info("Creating user with request: {}", userCreationRequest);

        if (userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + userCreationRequest.getEmail() + " is already taken. Please try another email.");
        }

        // Create user entity
        UserEntity userEntity = new UserEntity();
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

        // If the user's role is INSTRUCTOR, create an instructor entity
        if (userCreationRequest.getRole() == RoleEnum.INSTRUCTOR) {
            createInstructorProfile(savedUser, now);
        }

        return userMapper.entityToResponse(savedUser);
    }

    private void createInstructorProfile(UserEntity user, LocalDateTime now) {
        log.info("Creating instructor profile for user ID: {}", user.getUserId());
        
        InstructorEntity instructor = new InstructorEntity();
        // Use the same ID as user ID for instructorId to make it predictable
        instructor.setUserId(user.getUserId());
        instructor.setInstructorName(user.getFullName());
        instructor.setProfilePicture(user.getAvatar());
        instructor.setBio(user.getDescription());
        instructor.setAverageRating(BigDecimal.ZERO); // Initial rating of 0
        instructor.setIsVerified(false); // New instructors start unverified
        instructor.setCreatedAt(now);
        instructor.setUpdatedAt(now);
        instructor.setIsPaypalVerified(false);
        
        instructorRepository.save(instructor);
        log.info("Instructor profile created successfully for user ID: {}", user.getUserId());
    }
}