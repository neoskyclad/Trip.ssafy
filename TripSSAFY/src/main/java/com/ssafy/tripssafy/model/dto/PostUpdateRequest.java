package com.ssafy.tripssafy.model.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// 공통 게시글 수정 request
public class PostUpdateRequest {
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	
	@NotBlank(message = "내용은 필수입니다.")
	private String content;
	
	private Integer rating;
	private MultipartFile file;
	private String imgUrl;
}
