package com.ssafy.tripssafy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.tripssafy.model.dto.ChangePasswordRequest;
import com.ssafy.tripssafy.model.dto.InfoResponse;
import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.SearchUserResponse;
import com.ssafy.tripssafy.model.dto.UpdatePasswordRequest;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.service.AuthService;
import com.ssafy.tripssafy.service.IdentityService;
import com.ssafy.tripssafy.service.S3Service;
import com.ssafy.tripssafy.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final S3Service s3Service;
	private final UserService userService;
	private final IdentityService identityService;
	private final AuthService authService;

	@Operation(summary = "마이페이지", description = "로그인된 유저의 정보를 가져옴")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "로드 성공. 유저 정보 반환"),
			@ApiResponse(responseCode = "400", description = "사용자를 찾을 수 없음"), })
	@GetMapping("/info")
	public ResponseEntity<?> getUserInfo() {
		try {
			Long userId = identityService.getCurrentUserId();
			User user = userService.getUserById(userId);

			// 필요한 정보만 JSON으로 응답하도록 DTO를 사용
			InfoResponse userInfo = new InfoResponse(user.getUsername(), user.getNickname(), user.getProfileImg(), user.getRole(),
					user.getCreatedAt());

			return ResponseEntity.ok(userInfo);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보를 찾을 수 없습니다.");
		}
	}

	@Operation(summary = "유저 검색", description = "아이디를 기반으로 유저를 검색함")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "유저 검색 성공. 유저의 정보를 반환함"),
			@ApiResponse(responseCode = "400", description = "사용자를 찾을 수 없음"), })
	@GetMapping
	public ResponseEntity<?> searchUser(@RequestParam("username") String username) {
		System.out.println(username);
		try {
			List<User> users = userService.searchUsersByNameOrNickname(username);
			List<SearchUserResponse> searchUserResponses = new ArrayList<>();
			
			for (User user : users) {
				SearchUserResponse searchUserResponse = new SearchUserResponse(user.getUsername(), user.getNickname(),
						user.getProfileImg());
				searchUserResponses.add(searchUserResponse);
			}

			return ResponseEntity.ok(searchUserResponses);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보를 찾을 수 없습니다.");
		}
	}

	@Operation(summary = "프로필 업로드", description = "로그인된 유저의 프로필을 갱신함")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "프로필 업로드 성공"),
			@ApiResponse(responseCode = "400", description = "사용자를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "이미지 서버로 업로드 할 수 없음") })
	@PostMapping("/profile-image")
	public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
		try {
			Long userId = identityService.getCurrentUserId();
			// 1. 기존 이미지 URL
			String oldImageUrl = userService.getUserById(userId).getProfileImg();
			System.out.println(oldImageUrl);
			// 기존 이미지가 있으면 S3서버에서 삭제
			if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
				s3Service.deleteFile(oldImageUrl);
			}

			// 2. 새 이미지 URL
			String imageUrl = s3Service.uploadFile(file);
			userService.updateProfileImage(userId, imageUrl);

			return ResponseEntity.ok(imageUrl);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 없음");
		}
	}

	@Operation(summary = "비밀번호 변경", description = "로그인된 유저의 비밀번호를 변경함")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
			@ApiResponse(responseCode = "400", description = "사용자를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "현재 비밀번호가 잘못됨") })
	@PostMapping("/password")
	public ResponseEntity<?> changePassword(@ModelAttribute UpdatePasswordRequest dto) {
		try {
			// 1. 현재 비밀번호 맞는지 체크
			Long userId = identityService.getCurrentUserId();
			String username = userService.getUserById(userId).getUsername();

			try {
				String auth = authService.login(new LoginRequest(username, dto.getCurrentPassword()));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("현재 비밀번호 틀림");
			}

			userService.updatePassword(username, dto.getNewPassword());

			return ResponseEntity.ok().build();

		} catch (UsernameNotFoundException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 없음");
		}
	}
}
