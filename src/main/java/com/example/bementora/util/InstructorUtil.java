package com.example.bementora.util;

import com.example.bementora.entity.InstructorEntity;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.exception.ResourceNotFoundException;
import com.example.bementora.repository.InstructorRepository;
import com.example.bementora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstructorUtil {
    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;

    public UUID extractInstructorId(Authentication authentication) {
        try {
            String userEmail = authentication.getName();

            UserEntity user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found" + userEmail));

            InstructorEntity instructor = instructorRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for user " + user.getUserId()));

            return instructor.getInstructorId();
        } catch (Exception e) {
            log.error("Error extracting instructor id: ", e);
            throw new RuntimeException("Failed to extract instructor information:",e);
        }
    }

    public UserEntity extractUser(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userEmail));
    }

    private InstructorEntity extractInstructor(Authentication authentication) {
        UserEntity user = extractUser(authentication);
        return instructorRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for user " + user.getUserId()));
    }
}
