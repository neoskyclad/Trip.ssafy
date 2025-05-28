package com.ssafy.tripssafy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.ExpenseAddRequest;
import com.ssafy.tripssafy.model.dto.GameResultAddRequest;
import com.ssafy.tripssafy.model.dto.PlaceExpenseResponse;
import com.ssafy.tripssafy.model.dto.RoomExpenseResponse;
import com.ssafy.tripssafy.repository.ExpenseRepository;
import com.ssafy.tripssafy.repository.TripPlaceRepository;
import com.ssafy.tripssafy.repository.TripRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
	
	private final ExpenseRepository expenseRepository;
	
	private final TripPlaceRepository tripPlaceRepository;
	
	private final TripRoomRepository tripRoomRepository;
	
	private final IdentityService identityService;
	
	@Transactional
	public void addExpenses(List<ExpenseAddRequest> requests) throws EntityNotFoundException {
		if(tripPlaceRepository.findPlaceById(requests.get(0).getTripPlaceId()) == null) {
			throw new EntityNotFoundException("해당 id의 trip place가 없습니다.");
		}
		for(ExpenseAddRequest request: requests) {
			expenseRepository.addExpense(request);
		}
	}
	
	public List<RoomExpenseResponse> getMyExpenses(Long roomId) throws EntityNotFoundException {
		if(tripRoomRepository.getTripRoomById(roomId) == null) {
			throw new EntityNotFoundException("해당 id의 trip room이 없습니다.");
		}
		Long myId = identityService.getCurrentUserId();

		return expenseRepository.getExpensesByRoomAndUserId(roomId, myId);
	}
	
	public List<PlaceExpenseResponse> getPlaceExpenses(Long placeId) throws EntityNotFoundException {
		if(tripPlaceRepository.findPlaceById(placeId) == null) {
			throw new EntityNotFoundException("해당 id의 trip place가 없습니다.");
		}
		return expenseRepository.getExpensesByPlaceId(placeId);
	}

}
