package com.ssafy.tripssafy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.model.dto.PromotionAddRequest;
import com.ssafy.tripssafy.model.entity.Promotion;
import com.ssafy.tripssafy.service.PromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotion")
@RequiredArgsConstructor
@Tag(name="PromotionController", description="여행사 회원이 여행 상품 프로모션을 올릴 수 있는 Controller")
public class PromotionController {

	private final PromotionService promotionService;
	
	//여행사 관계자나 admin만 추가 가능
	@Operation(summary = "프로모션 등록", description="프로모션을 등록합니다. admin이나 여행사 직원만 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로모션 등록 성공!"),
		@ApiResponse(responseCode = "400", description = "RequestDTO Validation Error"),
		@ApiResponse(responseCode = "403", description = "프로모션 등록 권한이 없습니다."),
		@ApiResponse(responseCode = "500", description = "파일 업로드 실패")
	})
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_TRAVEL_AGENCY', 'ROLE_ADMIN')")
	public ResponseEntity<?> addPromotion(@Valid @ModelAttribute PromotionAddRequest request){
		try {
			promotionService.addPromotion(request);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	//인증 없이 모두 메인 화면에서 볼 수 있어야 함
	@Operation(summary = "프로모션 불러오기", description = "RequestParam으로 받은 type에 대한 여행사 등록 프로모션들을 불러옵니다. 인증 없이 누구나 볼 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로모션 로드 성공!")
	})
	@GetMapping("/get")
	public ResponseEntity<?> getPromotions(@RequestParam("contentId") Long contentId){
		List<Promotion> promotions = promotionService.getLatestTen(contentId);
		
		return ResponseEntity.ok(promotions);
	}
	
	@Operation(summary = "프로모션 삭제", description = "프로모션을 삭제합니다. admin이나 프로모션 작성자만 삭제가 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로모션 로드 성공!"),
		@ApiResponse(responseCode = "400", description = "해당 id의 프로모션이 없습니다."),
		@ApiResponse(responseCode = "403", description = "프로모션 삭제 권한이 없습니다.")
	})
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN') or @promotionService.isOwner(#id)")
	public ResponseEntity<?> deletePromotion(@PathVariable("id") Long id){
		promotionService.deletePromotion(id);
		
		return ResponseEntity.ok().build();
	}
}
