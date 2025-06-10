package com.example.bementora.controller;

import com.example.bementora.common.ApiResponse;
import com.example.bementora.dto.request.AuthenticationRequest;
import com.example.bementora.dto.request.RefreshTokenRequest;
import com.example.bementora.dto.request.RegisterRequest;
import com.example.bementora.dto.response.AuthenticationResponse;
import com.example.bementora.service.AuthenticationService;
import com.example.bementora.service.TokenBlackListService;
import com.example.bementora.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CookieUtil cookieUtil;
    private final TokenBlackListService tokenBlackListService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody @Valid RegisterRequest request
    ) {
                log.info("Received request to register: {}", request);
                AuthenticationResponse response = authenticationService.register(request);

                // Set refreshToken in HTTP-only cookie
                ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(response.getRefreshToken());
                response.setRefreshToken(null);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(
                                new ApiResponse<>(
                                HttpStatus.OK.value(),
                                "User logged in successfully",
                                response)
                        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        log.info("Received request to login: {}", request);
        AuthenticationResponse response = authenticationService.login(request);

        // Set refreshToken in HTTP-only cookie
        ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(response.getRefreshToken());
        response.setRefreshToken(null);


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(
                        new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User logged in successfully",
                        response)
                );

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(HttpServletRequest request) {
        log.info("Refreshing token");

        String refreshToken = cookieUtil.extractRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);

        ResponseCookie newRefreshTokenCookie = cookieUtil.createRefreshTokenCookie(response.getRefreshToken());
        response.setRefreshToken(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                .body(
                        new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Token refreshed successfully",
                        response)
                );

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
                                       @RequestHeader("Authorization") String authHeader) {
        // Blacklist current access token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            tokenBlackListService.blackListAccessToken(accessToken);
        }

        // Get refresh token from cookie and invalidate in DB
        String refreshToken = cookieUtil.extractRefreshTokenFromCookies(request);
        if (refreshToken != null) {
            authenticationService.revokeRefreshToken(refreshToken);
        }

        // Clear the refresh token cookie
        ResponseCookie deleteRefreshTokenCookie = cookieUtil.deleteRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString())
                .build();
    }

}
