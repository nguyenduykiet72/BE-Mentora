package com.example.bementora.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.test")
@Data
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
