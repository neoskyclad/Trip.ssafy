package com.ssafy.tripssafy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.model.dto.BoardPostAddRequest;
import com.ssafy.tripssafy.model.dto.PostDetailResponse;
import com.ssafy.tripssafy.model.dto.PostItemResponse;
import com.ssafy.tripssafy.model.dto.PostPaginationResponse;
import com.ssafy.tripssafy.model.dto.PostUpdateRequest;
import com.ssafy.tripssafy.model.dto.ReviewPostAddRequest;
import com.ssafy.tripssafy.model.dto.RoomPostAddRequest;
import com.ssafy.tripssafy.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name="GeneralPostController", description="review, room, post 공통 게시글 controller입니다.")
public class GeneralPostController {
	
	private final PostService postService;
	
	@Operation(summary = "방 공지글 불러오기", description="방 id를 받아 방 공지글들을 (post_type='room') 불러옵니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 공지글 로드 성공")
	})
	@GetMapping("/room/{id}")
	public ResponseEntity<?> getPostsByRoomId(@PathVariable("id") Long id) {
		List<PostItemResponse> posts = postService.getRoomPosts(id);
		
		return ResponseEntity.ok(posts);
	}
	
	@Operation(summary = "리뷰 글 불러오기", description="여행지 contentId를 받아서 리뷰 게시글들을 불러옵니다.(post_type='review')")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행지 게시글 로드 성공")
	})
	@GetMapping("/review/{id}")
	public ResponseEntity<?> getReviewPosts(@PathVariable("id") Long id) {
		List<PostItemResponse> posts = postService.getReviewPosts(id);
		
		return ResponseEntity.ok(posts);
	}
	
	@Operation(summary = "게시글, 댓글 불러오기", description="게시글 id를 받아서 특정 게시글과 달린 댓글들을 불러옵니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 공지글 로드 성공"),
		@ApiResponse(responseCode = "400", description = "해당 id의 게시글이 없음.")
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> getPostWithComments(@PathVariable("id") Long id) {
		PostDetailResponse post = postService.getPostDetail(id);
		
		return ResponseEntity.ok(post);
	}
	
	@Operation(summary = "게시판 게시글 불러오기", description="공지 게시판 또는 Q&A 게시판의 게시글들을 불러옵니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시판 게시글 로드 성공")
	})
	@GetMapping("/notice")
	public ResponseEntity<?> getNoticePostWithSearchQuery(@RequestParam(name="search", required=false) String search
			, @RequestParam("page") int pageNo, @RequestParam("type") String type) {
		PostPaginationResponse posts = postService.getNoticePosts(search, pageNo, type);
		
		return ResponseEntity.ok(posts);
	}
	
	@Operation(summary = "방 게시글 업로드", description="여행 방 게시글을 업로드합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행 방 게시글 업로드 성공 "),
		@ApiResponse(responseCode = "400", description = "해당 id의 방이 없거나 RequestDto validation error"),
		@ApiResponse(responseCode = "500", description = "파일 첨부 실패!")
	})
	@PostMapping("/room")
	public ResponseEntity<?> addRoomPost(@Valid @ModelAttribute RoomPostAddRequest request) {
		try {
			postService.addRoomPost(request);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@Operation(summary = "리뷰 게시글 업로드", description="여행지 리뷰 게시글을 업로드합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "리뷰 게시글 업로드 성공"),
		@ApiResponse(responseCode = "400", description = "RequestDto validation error"),
		@ApiResponse(responseCode = "500", description = "파일 첨부 실패!")
	})
	@PostMapping("/review")
	public ResponseEntity<?> addReviewPost(@Valid @ModelAttribute ReviewPostAddRequest request) {
		try {
			postService.addReviewPost(request);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@Operation(summary = "게시판 게시글 업로드", description="Q&A 또는 공지 게시판 게시글을 업로드합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 공지글 로드 성공"),
		@ApiResponse(responseCode = "400", description = "RequestDto validation error"),
		@ApiResponse(responseCode = "500", description = "파일 첨부 실패!")
	})
	@PostMapping("/notice")
	public ResponseEntity<?> addNoticePost(@Valid @ModelAttribute BoardPostAddRequest request) {
		try {
			postService.addBoardPost(request);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@Operation(summary = "게시글 수정", description="공통 게시글 수정 api입니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 공지글 로드 성공"),
		@ApiResponse(responseCode = "400", description = "해당 id의 게시글이 없음., RequestDto validation error"),
		@ApiResponse(responseCode = "500", description = "파일 첨부 실패!")
	})
	@PatchMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @Valid @ModelAttribute PostUpdateRequest request) {
		try {
			postService.updatePost(request, id);
			return ResponseEntity.ok().build();
		
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 첨부 실패"); 
		}
	}
	
	@Operation(summary = "게시글 삭제", description="공통 게시글 삭제 api입니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
		@ApiResponse(responseCode = "400", description = "해당 id의 게시글이 없음."),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
		postService.deletePost(id);
		return ResponseEntity.ok().build();
	}
}
