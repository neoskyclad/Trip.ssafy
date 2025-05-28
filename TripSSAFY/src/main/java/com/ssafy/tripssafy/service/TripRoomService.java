package com.ssafy.tripssafy.service;

import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.DuplicateEntityException;
import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.TourSpotAddRequest;
import com.ssafy.tripssafy.model.dto.TripRoomAddRequest;
import com.ssafy.tripssafy.model.dto.TripRoomDetailResponse;
import com.ssafy.tripssafy.model.dto.TripPlaceSetMemoRequest;
import com.ssafy.tripssafy.model.entity.TripRoom;
import com.ssafy.tripssafy.repository.TripRoomRepository;
import com.ssafy.tripssafy.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripRoomService {

	private final TripRoomRepository tripRoomRepository;

	private final UserRepository userRepository;

	private final IdentityService identityService;

	@Transactional
	public void addRoom(TripRoomAddRequest request) throws DuplicateEntityException {
		// 방을 추가
		tripRoomRepository.addRoom(request);
		// 추가한 방에 나를 추가
		log.info("현재 유저: {}", identityService.getCurrentUsername());
		this.addMember(identityService.getCurrentUserId(), request.getId());

		// 만약 초대한 사람이 있다면,
		List<String> invitedUsernames = request.getInvitedUsernames();
		if (invitedUsernames != null && !invitedUsernames.isEmpty()) {
			// 추가한 방에 초대한 사람들을 추가
			for (String username : invitedUsernames) {
				Long userId = userRepository.findByUsername(username).get().getId();
				this.addMember(userId, request.getId());
			}
		}
	}

	@Transactional
	public void editRoom(TripRoomAddRequest request) throws DuplicateEntityException {
		tripRoomRepository.editRoom(request);
		
		// 만약 초대한 사람이 있다면,
		List<String> invitedUsernames = request.getInvitedUsernames();
		if (invitedUsernames != null && !invitedUsernames.isEmpty()) {
			// 추가한 방에 초대한 사람들을 추가
			for (String username : invitedUsernames) {
				Long userId = userRepository.findByUsername(username).get().getId();
				this.addMember(userId, request.getId());
			}
		}
	}

	public List<TripRoom> getMyRooms() {
		return tripRoomRepository.findRoomsByUserId(identityService.getCurrentUserId());
	}

	@Transactional
	public void deleteRoom(Long roomId) throws EntityNotFoundException {
		int deleteCnt = tripRoomRepository.deleteRoom(roomId);
		if (deleteCnt == 0) {
			throw new EntityNotFoundException("해당 id의 장소가 없습니다.");
		}
	}

	// tripRoom에 사람을 추가한다.
	// 이미 추가된 사용자를 또 추가하려는 경우 Exception을 발생시킨다.
	@Transactional
	@PreAuthorize("@tripRoomService.isMember(#tripRoomId)")
	public void addMember(Long userId, Long roomId) throws DuplicateEntityException {
		if (tripRoomRepository.existsUserInRoom(userId, roomId) != null) {
			throw new DuplicateEntityException("이미 추가된 사용자입니다.");
		} else {
			tripRoomRepository.addMember(userId, roomId);
		}
	}

	// neoskyclad: 05/14 tripRoomId로 파라미터가 안 넘어와서 주석 처리
	// @PreAuthorize("@tripRoomService.isMember(#tripRoomId)")
	public TripRoomDetailResponse getRoomDetail(Long tripRoomId) throws EntityNotFoundException {
		if (!isMember(tripRoomId)) {
			return null;
		}
		TripRoomDetailResponse detailResponse = new TripRoomDetailResponse();
		TripRoom tripRoom = tripRoomRepository.getTripRoomById(tripRoomId);
		if (tripRoom == null)
			throw new EntityNotFoundException("해당 아이디의 방이 없습니다.");
		detailResponse.setTripRoom(tripRoom);
		detailResponse.setMemberList(userRepository.getUsersByTripRoomId(tripRoomId));
		detailResponse.setTripPlaceList(tripRoomRepository.getPlacesByRoomId(tripRoomId));

		return detailResponse;
	}

	public boolean isMember(Long id) {
		return tripRoomRepository.existsUserInRoom(identityService.getCurrentUserId(), id) != null;
	}

}
