package com.example.bementora.service.impl;

import com.example.bementora.config.JwtProperties;
import com.example.bementora.entity.UserEntity;
import com.example.bementora.service.JwtService;
import com.example.bementora.service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;
    private final TokenBlackListService tokenBlackListService;

    @Override
    public String generateAccessToken(UserEntity user) {
        return generateToken(new HashMap<>(), user.getEmail(), jwtProperties.getAccessTokenExpiration());
    }

    @Override
    public String generateRefreshToken(UserEntity user) {
        return generateToken(new HashMap<>(), user.getEmail(), jwtProperties.getRefreshTokenExpiration());
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            String subject,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()))
                && !isTokenExpired(token)
                && !isTokenBlacklisted(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlackListService.isTokenBlacklisted(token) || isTokenExpired(token) ;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims  = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
