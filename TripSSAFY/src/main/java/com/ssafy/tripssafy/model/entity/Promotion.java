package com.ssafy.tripssafy.model.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Promotion {

	private @NonNull Long id;
	private @NonNull String img;
	private @NonNull String url;
	private @NonNull String title;
	private @NonNull String content;
	private @NonNull Long writerId;
	private @NonNull LocalDateTime createdAt;
}
