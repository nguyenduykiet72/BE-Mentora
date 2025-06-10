package com.example.bementora.service;

public interface TokenBlackListService {
    void blacklistToken(String token, long ttlMillis);
    boolean isTokenBlacklisted(String token);
    void blackListAccessToken(String token);
}
