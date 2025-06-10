package com.example.bementora.service.impl;

import com.example.bementora.config.JwtProperties;
import com.example.bementora.service.TokenBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    public void blacklistToken(String token, long ttlMillis) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key,"blacklisted", ttlMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

    @Override
    public void blackListAccessToken(String token) {
        long ttl = jwtProperties.getAccessTokenExpiration();
        blacklistToken(token, ttl);
    }
}
