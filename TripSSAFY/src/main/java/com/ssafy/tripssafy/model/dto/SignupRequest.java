package com.ssafy.tripssafy.model.dto;

import com.ssafy.tripssafy.model.entity.Role;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	
	@NotBlank(message = "아이디는 필수입니다.")
	private @NonNull String username;
	
	@NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
        message = "비밀번호는 8~20자 사이의 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
	private @NonNull String password;
	
	@NotBlank(message = "닉네임은 필수입니다.")
	private @NonNull String nickname;
	
	private Role role = Role.ROLE_USER;
}
