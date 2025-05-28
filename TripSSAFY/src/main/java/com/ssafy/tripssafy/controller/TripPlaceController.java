package com.ssafy.tripssafy.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.TourSpotAddRequest;
import com.ssafy.tripssafy.model.dto.TourSpotEditRequest;
import com.ssafy.tripssafy.model.dto.TripPlaceSetMemoRequest;
import com.ssafy.tripssafy.service.TripPlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
@Tag(name = "TripRoomController", description="여행 장소 CRUD Controller입니다. Authorization Header 토큰을 포함해야 합니다.")
public class TripPlaceController {
	
	private final TripPlaceService tripPlaceService;
	
	@Operation(summary = "여행 장소 추가", description = "여행 장소들을 여러 개 받아서 추가합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "장소들 추가 성공"),
	    @ApiResponse(responseCode = "400", description = "TourSpotAddRequest 형식 오류"),
	})
	@PostMapping
	public ResponseEntity<?> addSpot(@RequestBody List<TourSpotAddRequest> requests) {
		tripPlaceService.addPlace(requests);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행 장소 수정", description = "여행 장소들을 리스트로 받아서 덮어씌웁니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "장소들 수정 성공"),
		@ApiResponse(responseCode = "400", description = "TourSpotAddRequest 형식 오류"),
	})
	@PutMapping
	public ResponseEntity<?> editSpot(@RequestBody List<TourSpotEditRequest> requests) {
		tripPlaceService.editPlace(requests);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행 장소 삭제", description = "현재 방의 장소 한 개를 tripRoomPlace entity의 id를 받아 삭제합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "장소 삭제 성공"),
	    @ApiResponse(responseCode = "400", description = "해당 id의 장소가 없습니다."),
	})
	@DeleteMapping
	public ResponseEntity<?> deleteSpot(@RequestParam("id") Long tripRoomPlaceId) {
		tripPlaceService.deletePlace(tripRoomPlaceId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "장소 메모 등록", description = "현재 장소에 메모를 등록합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "장소 삭제 성공"),
	    @ApiResponse(responseCode = "400", description = "해당 id의 장소가 없습니다."),
	})
	@PatchMapping("/memo")
	public ResponseEntity<?> setMemo(@RequestBody TripPlaceSetMemoRequest request) {
		tripPlaceService.setMemoToPlace(request);
		return ResponseEntity.ok().build();
	}
}
