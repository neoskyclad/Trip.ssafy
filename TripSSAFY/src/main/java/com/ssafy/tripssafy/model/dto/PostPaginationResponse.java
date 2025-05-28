package com.ssafy.tripssafy.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostPaginationResponse {
	private int totalCount;
	private List<PostItemResponse> posts = new ArrayList<>();
}
