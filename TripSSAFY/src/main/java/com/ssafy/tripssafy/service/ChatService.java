package com.ssafy.tripssafy.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import com.ssafy.tripssafy.model.dto.ChatStartRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatClient chatClient;

	public String getTravelRecommendation(ChatStartRequest request) {
		String prompt = promptBuilder(request);
		System.out.println(prompt);
		ChatResponse chatResponse = chatClient.prompt().user(prompt).call().chatResponse(); 
		return chatResponse.getResult().getOutput().getText();
	}

	private String promptBuilder(ChatStartRequest request) {
		return """
				다음은 사용자의 여행 성향입니다:

				- 선호 지역: %s
				- 동행자: %s
				- 여행 스타일: %s

				위 정보를 참고하여 사용자 성향에 맞는 여행지를 2~3곳 추천해주세요.
				- 간단한 설명과 함께 알려주세요. 해당 여행지를 대표할 수 있는 이모지를 넣어주세요.
				- 마크다운 형식으로 작성하고, 여행지 이름만 **굵게 표시**해주세요. 설명은 일반 텍스트로 작성해주세요.
				""".formatted(request.getRegion(), request.getCompanion(), request.getStyle());
	}
}
