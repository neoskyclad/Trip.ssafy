package com.ssafy.tripssafy.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.CommentAddRequest;
import com.ssafy.tripssafy.model.dto.CommentUpdateRequest;
import com.ssafy.tripssafy.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name="CommentController", description="댓글 CRUD Controller입니다.")
public class CommentController {
	
	private final CommentService commentService;
	
	@Operation(summary = "댓글 등록", description = "게시글에 댓글을 등록합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "댓글 등록 성공!"),
	    @ApiResponse(responseCode = "400", description = "RequestDTO Validation Error"),
	})
	@PostMapping
	public ResponseEntity<?> addComment(@Valid @RequestBody CommentAddRequest request) {
		commentService.addComment(request);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "댓글 수정", description = "댓글 id를 받아 특정 댓글을 수정합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "댓글 수정 성공!"),
	    @ApiResponse(responseCode = "400", description = "해당 id의 댓글이 없음. / RequestDTO Validation Error"),
	})
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentUpdateRequest request) {
		commentService.updateComment(id, request.getContent());
		return ResponseEntity.ok().build();
		
	}
	
	@Operation(summary = "댓글 삭제", description = "댓글 id를 받아 특정 댓글을 삭제합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공!"),
	    @ApiResponse(responseCode = "400", description = "해당 id의 댓글이 없습니다."),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
		commentService.deleteComment(id);
		return ResponseEntity.ok().build();
	}
}
