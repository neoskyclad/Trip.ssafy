package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginResponse {
	private @NonNull Long username;
	private @NonNull String nickname;
	private @NonNull String role;
	private String accessToken;	// 모든 과정 인증에 사용 (15분~30분), js 변수에 담음
	private String refreshToken;	// AccessToken 만료되었을 때, 재발급에 사용 (AccessToken보다 더 길게)
	//HttpOnly 쿠키에 담음
}
