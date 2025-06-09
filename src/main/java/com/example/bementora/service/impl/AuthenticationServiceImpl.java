package com.example.bementora.service.impl;

import com.example.bementora.config.JwtProperties;
import com.example.bementora.dto.request.AuthenticationRequest;
import com.example.bementora.dto.request.RegisterRequest;
import com.example.bementora.dto.response.AuthenticationResponse;
import com.example.bementora.entity.RefreshTokenEntity;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.enums.RoleEnum;
import com.example.bementora.repository.RefreshTokenRepository;
import com.example.bementora.repository.UserRepository;
import com.example.bementora.service.AuthenticationService;
import com.example.bementora.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);

        // Revoke any existing tokens
        refreshTokenRepository.revokeAllTokensByUser(user);

        // Create new refresh token
        RefreshTokenEntity refreshToken = createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(RoleEnum.STUDENT);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsEmailVerified(false);

        UserEntity savedUser = userRepository.save(user);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(savedUser);
        RefreshTokenEntity refreshToken = createRefreshToken(savedUser);

        // Revoke any existing tokens (optional for new users)
        refreshTokenRepository.revokeAllTokensByUser(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        RefreshTokenEntity token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired or revoked");
        }

        UserEntity user = token.getUser();

        String accessToken = jwtService.generateAccessToken(user);

        refreshTokenRepository.delete(token);
        RefreshTokenEntity newRefreshToken = createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    private RefreshTokenEntity createRefreshToken(UserEntity user) {
        String token = UUID.randomUUID().toString();

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(token)
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
