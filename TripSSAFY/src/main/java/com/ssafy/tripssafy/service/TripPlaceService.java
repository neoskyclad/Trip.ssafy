package com.ssafy.tripssafy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.TourSpotAddRequest;
import com.ssafy.tripssafy.model.dto.TourSpotEditRequest;
import com.ssafy.tripssafy.model.dto.TripPlaceSetMemoRequest;
import com.ssafy.tripssafy.repository.TripPlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripPlaceService {
	
	private final TripPlaceRepository tripRoomPlaceRepository;
	
	//tripRoom에 장소를 추가합니다.
	@Transactional
	public void addPlace(List<TourSpotAddRequest> requests) {
		for(TourSpotAddRequest request: requests) {
			tripRoomPlaceRepository.addPlace(request);
		}
	}
	
	public void editPlace(List<TourSpotEditRequest> requests) {
		for (int spotIndex = 0; spotIndex < requests.size(); spotIndex++) {
			tripRoomPlaceRepository.editPlace(requests.get(spotIndex).getId(), spotIndex);
		}
	}
	
	//tripRoom에서 장소를 삭제합니다.
	@Transactional
	public void deletePlace(Long tripRoomPlaceId) throws EntityNotFoundException {
		int deleteCnt = tripRoomPlaceRepository.deletePlace(tripRoomPlaceId);
		if(deleteCnt == 0) {
			throw new EntityNotFoundException("해당 id의 장소가 없습니다.");
		}
	}
	
	//tiprRoomPlace에 메모를 기록합니다.
	@Transactional
	public void setMemoToPlace(TripPlaceSetMemoRequest request) throws EntityNotFoundException {
		int updateCnt = tripRoomPlaceRepository.setMemo(request);
		if(updateCnt == 0) {
			throw new EntityNotFoundException("해당 id의 장소가 없습니다.");
		}
	}

}
