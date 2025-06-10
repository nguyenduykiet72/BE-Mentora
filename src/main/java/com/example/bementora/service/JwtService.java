package com.example.bementora.service;

import com.example.bementora.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserEntity userEntity);

    String generateRefreshToken(UserEntity userEntity);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenBlacklisted(String token);
}
