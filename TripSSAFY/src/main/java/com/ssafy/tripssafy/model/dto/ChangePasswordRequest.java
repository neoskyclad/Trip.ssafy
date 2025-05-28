package com.ssafy.tripssafy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequest {
	@NotBlank(message = "현재 비밀번호는 필수입니다.")
	private String currentPassword;
	
	@NotBlank(message = "새 비밀번호를 입력하세요.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
        message = "비밀번호는 8~20자 사이의 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
	private String newPassword;
}
