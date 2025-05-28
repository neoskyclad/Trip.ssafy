package com.ssafy.tripssafy.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.DuplicateEntityException;
import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.SignupRequest;
import com.ssafy.tripssafy.model.entity.Role;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.repository.UserRepository;
import com.ssafy.tripssafy.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final JWTUtil jwtUtil;
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	@Override
	public String login(LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("해당 아이디의 유저를 찾을 수 없습니다."));
		
		//비밀번호 맞음
		if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			//jwt 토큰 발행
			String accessToken = jwtUtil.generateAccessToken(user);
			
			return accessToken;
		} else {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}
	}
	
	@Override
	@Transactional
	public void signup(SignupRequest signupRequest) throws DuplicateEntityException {
		//아이디 중복 확인
		Optional<User> user = userRepository.findByUsername(signupRequest.getUsername());
		if(user.isPresent()) {
			throw new DuplicateEntityException("아이디가 이미 사용중입니다.");
		}
		
		//비밀번호 암호화 후 유저 삽입
		signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		userRepository.insert(signupRequest);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
