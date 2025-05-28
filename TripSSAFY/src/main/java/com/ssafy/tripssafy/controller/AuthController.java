package com.ssafy.tripssafy.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.SignupRequest;
import com.ssafy.tripssafy.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	
	@Operation(summary = "로그인", description = "아이디, 패스워드를 받아 로그인")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "로그인 성공. Response Header에 Authorization jwt 토큰 포함"),
	    @ApiResponse(responseCode = "400", description = "아이디 또는 패스워드가 틀림 => 로그인 실패"),
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@ModelAttribute LoginRequest request) {
		try {
			String accessToken = authService.login(request);
			// AccessToken은 HTTP Header에 담아 전송.
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
			
			// return --> Server에서 Client로 데이터를 전송.
			return ResponseEntity.ok()
					.headers(httpHeaders)
					.build();
			
		} catch (Exception e) {
			//UsernameNotFoundException 또는 BadCredentialException
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", "아이디 또는 비밀번호가 틀렸습니다."));
		}
	}
	
	@Operation(summary = "회원가입", description = "아이디, 패스워드, 닉네임, Role을 받아서 회원가입")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
	    @ApiResponse(responseCode = "400", description = "회원 가입 실패 (중복 아이디 오류 or SignupRequest 형식 오류)"),
	})
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @ModelAttribute SignupRequest request) {
		
		try {
			authService.signup(request);
			
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", e.getMessage()));
		}
	}
}
