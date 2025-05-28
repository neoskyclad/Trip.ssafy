package com.ssafy.tripssafy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
	private @NonNull String currentPassword;
	private @NonNull String newPassword;
}
