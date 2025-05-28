package com.ssafy.tripssafy.model.dto;

import java.time.LocalDateTime;

import com.ssafy.tripssafy.model.entity.Post;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PostItemResponse {
	private Long id;
	private String profileImg;
	private @NonNull String nickname;
	private @NonNull String title;
	private @NonNull String content;
	private Integer rating;
	private String img;
	private @NonNull Long writerId;
	private Integer viewCount;
	private @NonNull LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean isMyPost = false;	//나의 게시글인지 아닌지 -> 나의 게시글만 수정, 삭제 UI 생
}
