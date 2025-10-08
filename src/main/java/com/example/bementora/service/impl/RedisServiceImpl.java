package com.example.bementora.service.impl;

import com.example.bementora.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate redisTemplate;
    private ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
            log.debug("set redis cart key:{} value:{}", key, value);
        } catch (JsonProcessingException e) {
            log.error("Error serializing object for key: {}", key, e);
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    @Override
    public void setWithExpiry(String key, Object value, long timeout) {
        setWithExpiry(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void setWithExpiry(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeout, timeUnit);
            log.debug("Stored key: {} in Redis with expiry: {} {}", key, timeout, timeUnit);
        } catch (JsonProcessingException e) {
            log.error("Error serializing object for key: {}", key, e);
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            String jsonValue = (String) redisTemplate.opsForValue().get(key);
            if (jsonValue == null) {
                log.debug("get redis cart key:{} null", key);
                return null;
            }

            T result = objectMapper.readValue(jsonValue, clazz);
            log.debug("Retrieved key: {} from Redis", key);
            return result;
        } catch (Exception e) {
            log.error("Error deserializing object for key: {}", key, e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
        log.debug("Deleted key: {} from Redis", key);
    }

    @Override
    public boolean hasKey(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null && exists;
    }

    @Override
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        log.debug("Set expiry for key: {} to {} seconds", key, timeout);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
}
