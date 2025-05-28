package com.ssafy.tripssafy.model.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class Post {
	private @NonNull Long id;
	private @NonNull String title;
	private String img;
	private @NonNull String content;
	private Integer rating;
	private @NonNull String postType;
	private @NonNull Long writerId;
	private Long roomId;
	private Integer viewCount;
	private @NonNull LocalDateTime createdAt;
	private @NonNull LocalDateTime updatedAt;
	
}
