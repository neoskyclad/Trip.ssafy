package com.ssafy.tripssafy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.model.dto.ChatStartRequest;
import com.ssafy.tripssafy.service.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService chatService;
	
	@Operation(summary = "AI 여행 챗봇 시작", description = "사용자로부터 선호 지역, 동행자, 여행 스타일을 입력 받아 답변을 반환")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "AI 답변 성공"),
	    @ApiResponse(responseCode = "400", description = "ChatGPT 서버 오류"),
	})
	@PostMapping("/start")
	public ResponseEntity<?> getTravelRecommendation(@RequestBody ChatStartRequest request) {
		String response = chatService.getTravelRecommendation(request);
		
		return ResponseEntity.ok(response);
	}
}
