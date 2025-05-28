package com.ssafy.tripssafy.model.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ReviewPostAddRequest {
	
	private @NonNull Long placeId;	//api의 place Id
	private Integer rating;	//별점
	@NotBlank(message = "제목은 필수입니다.")
	private String title;	//제목 
	@NotBlank(message = "내용은 필수입니다.")
	private String content;	//내용
	private String imgUrl;	//s3에 올린 image url, file을 올린 후에 set
	private MultipartFile file;	//이미지 파일만 허용한다.
}
