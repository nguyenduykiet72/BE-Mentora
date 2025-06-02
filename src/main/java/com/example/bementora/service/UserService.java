package com.example.bementora.service;

import com.example.bementora.dto.request.UserCreationRequest;
import com.example.bementora.dto.response.UserCreationResponse;
import com.example.bementora.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreationResponse findById(UUID userId);

    List<UserEntity> getAllUser();

    UserCreationResponse createUser(UserCreationRequest userCreationRequest);
}
