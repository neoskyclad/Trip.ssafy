package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateProfileRequest {
	private @NonNull Long id;
	private @NonNull String profileImg;
}
