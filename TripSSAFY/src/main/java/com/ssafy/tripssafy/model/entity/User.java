package com.ssafy.tripssafy.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class User {
	
	private @NonNull Long id;
	private @NonNull String username;
	private @NonNull String password;
	private @NonNull String nickname;
	private String profileImg;
	private Role role;
	private @NonNull String createdAt;
	

}
