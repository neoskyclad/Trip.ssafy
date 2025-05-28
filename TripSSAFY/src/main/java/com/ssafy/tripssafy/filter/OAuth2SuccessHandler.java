package com.ssafy.tripssafy.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssafy.tripssafy.exception.DuplicateEntityException;
import com.ssafy.tripssafy.model.dto.CustomOAuth2User;
import com.ssafy.tripssafy.model.dto.SignupRequest;
import com.ssafy.tripssafy.model.entity.Role;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.service.AuthService;
import com.ssafy.tripssafy.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	private final JWTUtil jwtUtil;
	
	private final AuthService authService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = (String) oauth2User.getAttributes().get("email");
        String nickname = (String) oauth2User.getAttributes().get("nickname");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is missing from OAuth2 provider.");
            return;
        }

        // 사용자 존재 여부 확인
        Optional<User> user = authService.findByUsername(email);

        if (user.isEmpty()) {
            // 사용자 신규 등록 (패스워드는 정규식 만족하는 아무 문자열로 처리함)
            SignupRequest signupRequest = new SignupRequest(email, "1234@@@@", nickname, Role.ROLE_USER);
            try {
				authService.signup(signupRequest);
			} catch (DuplicateEntityException e) {
				// 사실상 여기 올 일 없음
				e.printStackTrace();
			}
            
            user = authService.findByUsername(email);
        }

        // Access Token 발급
        String accessToken = jwtUtil.generateAccessToken(user.get());

        // Authorization 헤더에 JWT 포함
        response.setHeader("Authorization", "Bearer " + accessToken);
        
        response.sendRedirect("http://localhost:5173?token="+accessToken);
    }
	
	
}
