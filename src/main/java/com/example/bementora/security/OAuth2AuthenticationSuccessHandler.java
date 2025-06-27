package com.example.bementora.security;

import com.example.bementora.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;

    @Value("${application.oauth2.success-redirect-url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(oAuth2User.getUser());
        String refreshToken = jwtService.generateRefreshToken(oAuth2User.getUser());

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("accessToken",accessToken)
                .queryParam("refreshToken",refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }

}
