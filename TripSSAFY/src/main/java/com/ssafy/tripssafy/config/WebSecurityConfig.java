package com.ssafy.tripssafy.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.tripssafy.filter.JWTFilter;
import com.ssafy.tripssafy.filter.OAuth2SuccessHandler;
import com.ssafy.tripssafy.model.dto.CustomUserDetails;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.service.CustomOAuth2UserService;
import com.ssafy.tripssafy.service.CustomUserDetailService;
import com.ssafy.tripssafy.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Configuration // 이 클래스는 설정 파일용 명시
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JWTUtil jwtUtil;

	private final CustomUserDetailService customUserDetailsService;
	
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	//인증 없이 허용 url
	private static String[] NO_AUTH_URL = {"/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "index.html", "/", "/assets/**", "/tour-spot/**","login.html", "/chat/**", "/promotion/get", "/post/notice", "/post/{id}"};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// 같은 Origin? : URL + PORT
		// CORS: 브라우저 정책 URL + PORT가 다르면? 허가 안됨. --> 그래서 허용
		httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
				.csrf(AbstractHttpConfigurer::disable) // CSRF: XSS랑 혼동 X
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session 사용 안함
				.addFilterBefore(new JWTFilter(jwtUtil, customUserDetailsService),
						UsernamePasswordAuthenticationFilter.class)
				.formLogin(AbstractHttpConfigurer::disable) // Spring Security 기본 로그인 비활성화.
				.authorizeHttpRequests(
						authorize -> authorize.requestMatchers(NO_AUTH_URL).permitAll()
						.anyRequest().authenticated() //나머지 요청은 확인 필요
				)
				.oauth2Login(oauth2 -> 
						oauth2.userInfoEndpoint(c -> c.userService(customOAuth2UserService()))
						.successHandler(oAuth2SuccessHandler)
				)
				.httpBasic(AbstractHttpConfigurer::disable);// Spring Security 인증 메커니즘 비활성화.

		return httpSecurity.build();
	}

	// Preflight 요청 OPTIONS Method: GET, POST, PUT ... 를 확인
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // 특정 출처만 허용
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With")); // 허용할 헤더
		configuration.setExposedHeaders(Collections.singletonList("Authorization")); // 노출이 허용된 헤더
		configuration.setAllowCredentials(true); // 자격 증명 허용 (쿠키 등)
		configuration.setMaxAge(3600L); // Preflight 요청 결과를 캐시하는 시간 (초)

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 URL에 위의 CORS 설정 적용

		return source;
	}

	@Bean
	public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}
