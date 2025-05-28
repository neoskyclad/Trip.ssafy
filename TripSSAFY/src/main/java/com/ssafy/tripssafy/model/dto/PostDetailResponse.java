package com.ssafy.tripssafy.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

//게시글과 댓글을 모두 가져오는 dto
@Data
@NoArgsConstructor
public class PostDetailResponse {

	private PostItemResponse post;
	
	private List<CommentItemResponse> comments = new ArrayList<>();
}
