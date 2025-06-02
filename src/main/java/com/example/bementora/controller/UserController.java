package com.example.bementora.controller;

import com.example.bementora.common.ApiResponse;
import com.example.bementora.dto.request.UserCreationRequest;
import com.example.bementora.dto.response.UserCreationResponse;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public Map<String, Object> getUserById(@PathVariable("userId") UUID userId) {
        UserCreationResponse userResponse = userService.findById(userId);
        log.info("userResponse: {}", userResponse);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", String.valueOf(HttpStatus.OK.value()));
        result.put("message", "user detail");
        result.put("data", userResponse);  // Bỏ String.valueOf() để return object thay vì string

        return result;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserEntity>>> getAllUsers() {
        List<UserEntity> users = userService.getAllUser();
        ApiResponse<List<UserEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "All users retrieved successfully",
                users
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserCreationResponse>> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        log.info("Received request to create user: {}", userCreationRequest);
        
        UserCreationResponse createdUser = userService.createUser(userCreationRequest);
        
        ApiResponse<UserCreationResponse> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            "User created successfully",
            createdUser
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
