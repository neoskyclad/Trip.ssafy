package com.ssafy.tripssafy.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.tripssafy.model.entity.TripRoom;
import com.ssafy.tripssafy.model.entity.TripRoomPlace;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TripRoomDetailResponse {
	//room 기본 정보
	private TripRoom tripRoom;
	//현재 방에 소속된 사람들 정보
	private List<RoomMemberResponse> memberList = new ArrayList<>();
	//현재 방에 추가된 방문 장소들
	private List<TripRoomPlace> tripPlaceList = new ArrayList<>();
}
