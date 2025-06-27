package com.example.bementora.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserService<R extends OAuth2UserRequest, U extends OAuth2User> {
    U loadUser(R userRequest) throws OAuth2AuthenticationException;
}
