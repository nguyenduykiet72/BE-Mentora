package com.example.bementora.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void set(String key, Object value);
    void setWithExpiry(String key, Object value, long timeout);
    void setWithExpiry(String key, Object value, long timeout, TimeUnit timeUnit);
    <T> T get(String key, Class<T> clazz);
    void delete(String key);
    boolean hasKey(String key);
    void expire(String key, long timeout);
    Long getExpire(String key);
}
