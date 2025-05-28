package com.ssafy.tripssafy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentAddRequest {
	@NotNull(message="게시글 id 필수값입니다.")
	private Long postId;
	@NotBlank(message="내용은 필수값입니다.")
	private String content;
}
