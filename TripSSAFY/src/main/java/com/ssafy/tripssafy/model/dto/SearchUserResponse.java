package com.ssafy.tripssafy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {
	@NonNull String username;
	@NonNull String nickname;
	String profileImage;
}
