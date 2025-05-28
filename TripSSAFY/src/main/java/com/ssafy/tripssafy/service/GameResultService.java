package com.ssafy.tripssafy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.GameResultAddRequest;
import com.ssafy.tripssafy.repository.GameResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameResultService {
	
	private final GameResultRepository gameResultRepository;
	
	@Transactional
	public void addGameResult(GameResultAddRequest request) {
		gameResultRepository.addResult(request);
	}
	
	public String getResultURL(Long placeId) {
		String html = gameResultRepository.getResultURL(placeId);
		
		return html;
	}
}
