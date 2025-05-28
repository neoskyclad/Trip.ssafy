package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
//장소에 메모를 등록하는 DTO
public class TripPlaceSetMemoRequest {
	
	private @NonNull Long tripRoomPlaceId;
	
	private String memo;
}
