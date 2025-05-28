package com.ssafy.tripssafy.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ssafy.tripssafy.model.dto.CustomOAuth2User;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "naver", "kakao"

        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        if (registrationId.equals("naver")) {
            attributes = parseNaverAttributes(attributes);
        } else if (registrationId.equals("kakao")) {
            attributes = parseKakaoAttributes(attributes);
        }

        return new CustomOAuth2User(attributes);
    }

    private Map<String, Object> parseNaverAttributes(Map<String, Object> attributes) {
        // attributes: {resultcode=00, message=success, response={id=..., email=..., nickname=...}}
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return Map.of(
            "id", response.get("id"),
            "email", response.get("email"),
            "nickname", response.get("nickname")
        );
    }

    private Map<String, Object> parseKakaoAttributes(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return Map.of(
            "id", attributes.get("id"),
            "email", kakaoAccount.get("email"),
            "nickname", profile.get("nickname")
        );
    }
}

