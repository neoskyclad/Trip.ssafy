package com.ssafy.tripssafy.model.dto;

import com.ssafy.tripssafy.model.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class InfoResponse {
	private @NonNull String username;
	private @NonNull String nickname;
	private String profileImage;
	private @NonNull Role role;
	private @NonNull String createdAt;
}
