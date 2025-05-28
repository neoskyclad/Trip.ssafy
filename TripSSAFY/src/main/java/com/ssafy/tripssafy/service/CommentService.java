package com.ssafy.tripssafy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.CommentAddRequest;
import com.ssafy.tripssafy.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	private final IdentityService identityService;
	
	@Transactional
	public void addComment(CommentAddRequest request) {
		Long myId = identityService.getCurrentUserId();
		
		commentRepository.addComment(request, myId);
	}
	
	@Transactional
	public void updateComment(Long id, String content) throws EntityNotFoundException {
		if(commentRepository.updateComment(id, content) == 0) {
			throw new EntityNotFoundException("해당 id의 comment가 존재하지 않습니다.");
		}
	}
	
	@Transactional
	public void deleteComment(Long id) throws EntityNotFoundException {
		if(commentRepository.deleteComment(id) == 0) {
			throw new EntityNotFoundException("해당 id의 comment가 존재하지 않습니다.");
		}
	}
}
