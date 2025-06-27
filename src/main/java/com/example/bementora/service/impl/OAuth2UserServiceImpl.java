package com.example.bementora.service.impl;

import com.example.bementora.entity.UserEntity;
import com.example.bementora.enums.AuthProviderEnum;
import com.example.bementora.enums.RoleEnum;
import com.example.bementora.repository.UserRepository;
import com.example.bementora.security.CustomOAuth2User;
import com.example.bementora.security.OAuth2UserInfo;
import com.example.bementora.security.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception e) {
            log.error("Failed to load user: {}", e.getMessage());
            throw new OAuth2AuthenticationException("Error processing OAuth user");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<UserEntity> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserEntity user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return CustomOAuth2User.create(user, oAuth2User.getAttributes());
    }

    private UserEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        log.info("Registering new user with email: {}", oAuth2UserInfo.getEmail());

        UserEntity user = new UserEntity();
        user.setProviderId(oAuth2UserInfo.getId());
        user.setProvider(AuthProviderEnum.valueOf(
                oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()
        ));
        user.setFullName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setAvatarUrl(oAuth2UserInfo.getImageUrl());
        user.setRole(RoleEnum.STUDENT);
        user.setEmailVerified(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setAvatarUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

}
