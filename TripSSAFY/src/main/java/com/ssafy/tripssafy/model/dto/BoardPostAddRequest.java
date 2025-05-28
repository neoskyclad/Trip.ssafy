package com.ssafy.tripssafy.model.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
// 공지 게시글
public class BoardPostAddRequest {

	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	@NotBlank(message = "내용은 필수입니다.")
	private String content;
	@NotBlank(message = "게시글 타입은 필수입니다.")
	private String type;
	private String imgUrl;
	private MultipartFile file;
}

