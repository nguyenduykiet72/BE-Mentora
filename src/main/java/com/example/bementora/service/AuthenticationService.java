package com.example.bementora.service;

import com.example.bementora.dto.request.AuthenticationRequest;
import com.example.bementora.dto.request.RegisterRequest;
import com.example.bementora.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    AuthenticationResponse refreshToken(String refreshToken);

    void revokeRefreshToken(String refreshToken);
}
