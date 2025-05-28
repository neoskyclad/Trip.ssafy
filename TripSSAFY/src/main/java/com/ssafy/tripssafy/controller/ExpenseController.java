package com.ssafy.tripssafy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.tripssafy.model.dto.ExpenseAddRequest;
import com.ssafy.tripssafy.model.dto.GameResultAddRequest;
import com.ssafy.tripssafy.model.dto.PlaceExpenseResponse;
import com.ssafy.tripssafy.model.dto.RoomExpenseResponse;
import com.ssafy.tripssafy.service.ExpenseService;
import com.ssafy.tripssafy.service.GameResultService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
@Tag(name="ExpenseController", description="여행 경비 정산 Controller입니다.")
public class ExpenseController {
	
	private final ExpenseService expenseService;
	
	private final GameResultService gameResultService;
	// 정산 내역 추가
	// 여행 장소 (trip place table) id를 받아서 모든 멤버 별 지출 비용을 table에 추가한다.
	@Operation(summary = "정산 내역들을 추가", description="해당 방에 있는 사람들의 정산 내역을 한번에 다 추가합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정산 내역 등록 성공!"),
		@ApiResponse(responseCode = "400", description = "RequestDTO Validation Error")
	})
	@PostMapping
	public ResponseEntity<?> addExpenses(@Valid @RequestBody List<ExpenseAddRequest> request) {
		expenseService.addExpenses(request);
		
		return ResponseEntity.ok().build();
	}

//	
//	//해당 방에서 내가 지출한 내역 불러오기
	@Operation(summary = "나의 정산 내역 불러오기", description="해당 방에서 내가 지출한 정산 내역들을 불러옵니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정산 내역 로드"),
		@ApiResponse(responseCode = "400", description = "해당 id의 room이 없습니다.")
	})
	@GetMapping("/room/{room-id}")
	public ResponseEntity<?> getMyExpenses(@PathVariable("room-id") Long roomId) {
		List<RoomExpenseResponse> response = expenseService.getMyExpenses(roomId);
		
		return ResponseEntity.ok(response);
	}
	
	//해당 장소의 멤버들 지출 내역 불러오기
	@Operation(summary = "장소 정산 내역 불러오기", description="해당 장소(placeId)에서 멤버들의 정산 내역들을 불러옵니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "장소 정산 내역 로드 성공!"),
		@ApiResponse(responseCode = "400", description = "해당 id의 tripPlace가 없습니다.")
	})
	@GetMapping("/{place-id}")
	public ResponseEntity<?> getPlaceExpenses(@PathVariable("place-id") Long placeId) {
		List<PlaceExpenseResponse> response = expenseService.getPlaceExpenses(placeId);
		
		return ResponseEntity.ok(response);
	}
	
	//게임 결과 전송
	@Operation(summary = "룰렛 게임 결과 이미지 전송", description="정산 룰렛 게임 결과 이미지를 전송합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "결과 이미지 전송 성공!"),
		@ApiResponse(responseCode = "400", description = "RequestDTO Validation Error")
	})
	@PostMapping("/game-result")
	public ResponseEntity<?> addGameResult(@Valid @RequestBody GameResultAddRequest request) {
		gameResultService.addGameResult(request);
		
		return ResponseEntity.ok().build();
	}
	
	//게임 결과 로드
	@Operation(summary = "룰렛 게임 결과 이미지 로드", description="tripPlaceId를 받아 룰렛 게임 결과가 있을 경우 로드합니다. 룰렛 게임을 하지 않고 정산했다면 null일 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "결과 이미지 로드 성공!"),
	})
	@GetMapping("/game-result/{id}")
	public ResponseEntity<?> getGameResult(@PathVariable("id") Long id) {
		String resultHTML = gameResultService.getResultURL(id);
		
		return ResponseEntity.ok(resultHTML);
	}
}
