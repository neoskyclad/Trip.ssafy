package com.ssafy.tripssafy.model.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TourSpotAddRequest {
	private @NonNull Long roomId;	//장소를 추가할 roomId
	private @NonNull Long placeId;	//api의 contentTypeId
	private @NonNull String placeName;	//api의 title
	private @NonNull Double mapX;	//api의 mapx
	private @NonNull Double mapY;	//api의 mapy
	private @NonNull LocalDate visitDate;	
	private @NonNull Integer visitOrder;
}
