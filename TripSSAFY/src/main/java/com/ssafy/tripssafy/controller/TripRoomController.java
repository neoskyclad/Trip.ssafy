package com.ssafy.tripssafy.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tripssafy.exception.DuplicateEntityException;
import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.TourSpotAddRequest;
import com.ssafy.tripssafy.model.dto.TripRoomAddRequest;
import com.ssafy.tripssafy.model.dto.TripRoomDetailResponse;
import com.ssafy.tripssafy.model.entity.TripRoom;
import com.ssafy.tripssafy.model.entity.User;
import com.ssafy.tripssafy.service.AuthService;
import com.ssafy.tripssafy.service.TripRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "TripRoomController", description = "여행 방 CRUD Controller입니다. Authorization Header 토큰을 포함해야 합니다.")
public class TripRoomController {

	private final TripRoomService tripRoomService;

	private final AuthService authService;

	@Operation(summary = "방 만들기", description = "여행 방을 생성합니다.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "여행 방 생성 및 현재 사용자 추가 성공!"),
			@ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@PostMapping
	public ResponseEntity<?> createRoom(@RequestBody TripRoomAddRequest request) {
		try {
			System.out.println(request);
			tripRoomService.addRoom(request);
			return ResponseEntity.ok().build();
		} catch (DuplicateEntityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", e.getMessage()));
		}
	}

	@Operation(summary = "방 수정하기", description = "방 id를 기준으로 여행 방을 수정합니다.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "여행 방 수정 성공"),
			@ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@PutMapping
	public ResponseEntity<?> editRoom(@RequestBody TripRoomAddRequest request) {
		try {
			tripRoomService.editRoom(request);
			return ResponseEntity.ok().build();
		} catch (DuplicateEntityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", e.getMessage()));
		}
	}

	@Operation(summary = "방 삭제", description = "여행 방을 삭제합니다. (삭제 정책을 아직 정하지 않아서 프론트 구현은 보류..)")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "여행 방 삭제 성공"),
			@ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@DeleteMapping
	public ResponseEntity<?> deleteRoom(@RequestParam("id") Long roomId) {
		try {
			tripRoomService.deleteRoom(roomId);
			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", e.getMessage()));
		}
	}

	@Operation(summary = "사용자 초대", description = "사용자의 id를 받아서 다른 사용자를 현재 방에 초대합니다.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "사용자 초대 성공!"),
			@ApiResponse(responseCode = "400", description = "이미 추가한 사용자 중복 추가 오류, 사용자 없는 오류 or requestParam 포함하지 않은 오류"),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@PostMapping("/{room_id}/invite")
	public ResponseEntity<?> invite(@PathVariable("room_id") Long roomId, @RequestParam("username") String username) {
		try {
			Optional<User> user = authService.findByUsername(username);

			if (user.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", "존재하지 않은 아이디입니다."));
			}

			tripRoomService.addMember(user.get().getId(), roomId);
			return ResponseEntity.ok().build();
		} catch (DuplicateEntityException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMsg", e.getMessage()));
		}
	}

	@Operation(summary = "내가 추가된 방들 가져오기", description = "현재 사용자가 소속된 방들을 모두 가져옵니다.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "방들 로드 성공!"),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@GetMapping
	public ResponseEntity<?> getMyRooms() {
		List<TripRoom> myTripRooms = tripRoomService.getMyRooms();

		return ResponseEntity.ok(myTripRooms);
	}

	@Operation(summary = "현재 방 정보 가져오기", description = "현재 방에 추가된 사용자들과 장소들을 모두 가져옵니다.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "방 정보 로드 성공!"),
			@ApiResponse(responseCode = "400", description = "해당 id의 방이 없습니다."),
			@ApiResponse(responseCode = "401", description = "사용자 인증 실패"), })
	@GetMapping("/{id}")
	public ResponseEntity<?> getRoomInfo(@PathVariable("id") Long id) {
		TripRoomDetailResponse response;
		response = tripRoomService.getRoomDetail(id);
		return ResponseEntity.ok(response);
	}
}
