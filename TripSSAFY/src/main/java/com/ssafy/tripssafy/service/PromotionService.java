package com.ssafy.tripssafy.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.PromotionAddRequest;
import com.ssafy.tripssafy.model.entity.Promotion;
import com.ssafy.tripssafy.repository.PromotionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionService {

	private final PromotionRepository promotionRepository;
	
	private final IdentityService identityService;
	
	private final S3Service s3Service;
	
	//최신 10개의 promotion들만 가져와서 메인 화면에 띄웁니다.
	public List<Promotion> getLatestTen(Long id){
		return promotionRepository.getLatestTen(id);
	}
	
	@Transactional
	public void addPromotion(PromotionAddRequest request) throws IOException {
		Long myId = identityService.getCurrentUserId();
		if(request.getFile() != null) {
			String imageUrl = s3Service.uploadFile(request.getFile());
			request.setImgUrl(imageUrl);
		}
		promotionRepository.insert(request, myId);
	}
	
	@Transactional
	public void deletePromotion(Long id) {
		if(promotionRepository.delete(id) == 0) {
			throw new EntityNotFoundException("해당 id의 promotion이 없습니다.");
		}
	}
	
	public boolean isOwner(Long promotionId) {
		log.info("promotionID: {}", promotionId);
		Promotion promotion = promotionRepository.findById(promotionId);
		
		return identityService.getCurrentUserId() == promotion.getWriterId();
	}
}
