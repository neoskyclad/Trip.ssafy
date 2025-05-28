package com.ssafy.tripssafy.model.dto;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PromotionAddRequest {
	
	@NonNull
	private Long contentId;

	@NotBlank(message = "제목은 필수값입니다.")
	private String title;
	
	@NotBlank(message = "내용은 필수값입니다.")
	private String content;
	
	private String imgUrl;
	
	@NotBlank(message = "url은 필수값입니다.")
	@URL(message = "url 형식에 맞지 않습니다.")
	private String url;
	
	private MultipartFile file;
}
