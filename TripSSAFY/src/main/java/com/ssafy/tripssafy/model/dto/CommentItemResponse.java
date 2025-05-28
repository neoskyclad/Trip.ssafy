package com.ssafy.tripssafy.model.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CommentItemResponse {
	private @NonNull Long id;
	private String profileImg;
	private @NonNull String nickname;
	private @NonNull String content;
	private @NonNull Long writerId;
	private @NonNull LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean isMyComment = false;	//내 댓글인 경우 수정 삭제 버튼 활성화
}
