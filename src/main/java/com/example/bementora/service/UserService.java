package com.example.bementora.service;

import com.example.bementora.dto.request.UserCreationRequest;
import com.example.bementora.dto.response.UserCreationResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreationResponse findById(UUID userId);

    List<UserCreationResponse> getAllUser();

    UserCreationResponse createUser(UserCreationRequest userCreationRequest);
}
