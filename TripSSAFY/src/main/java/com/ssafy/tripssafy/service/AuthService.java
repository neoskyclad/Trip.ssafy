package com.ssafy.tripssafy.service;

import java.util.Optional;

import com.ssafy.tripssafy.exception.DuplicateEntityException;
import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.SignupRequest;
import com.ssafy.tripssafy.model.entity.Role;
import com.ssafy.tripssafy.model.entity.User;

public interface AuthService {
	public String login(LoginRequest loginRequest);
	public void signup(SignupRequest signupRequest) throws DuplicateEntityException;
	public Optional<User> findByUsername(String username);
}
