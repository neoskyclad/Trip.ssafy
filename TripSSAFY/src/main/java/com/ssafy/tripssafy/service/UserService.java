package com.ssafy.tripssafy.service;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.UpdateProfileRequest;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public User getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));
		return user;
	}

	public List<User> searchUsersByNameOrNickname(String username) {
		List<User> users = userRepository.findByUsernameOrNickname(username);
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
		}
		return users;
	}

	@Transactional
	public void updateProfileImage(Long id, String imageUrl) {
		UpdateProfileRequest updateUserRequest = new UpdateProfileRequest(id, imageUrl);
		int result = userRepository.updateProfileImage(updateUserRequest);
		if (result == 0) {
			throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
		}
	}

	public void updatePassword(String username, String newPassword) {
		String encode = passwordEncoder.encode(newPassword);
		LoginRequest loginRequest = new LoginRequest(username, encode);
		int result = userRepository.updatePassword(loginRequest);
		if (result == 0) {
			throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
		}
	}

}
