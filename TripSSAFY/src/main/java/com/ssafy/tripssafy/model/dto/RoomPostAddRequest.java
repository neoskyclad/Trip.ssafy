package com.ssafy.tripssafy.model.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
// 여행 방 게시글 
public class RoomPostAddRequest {
	private @NonNull Long roomId;
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	@NotBlank(message = "내용은 필수입니다.")
	private String content;
	private String imgUrl;
	private MultipartFile file;
}
