package com.ssafy.tripssafy.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class TripRoomPlace {
	private @NonNull Long id;
	private @NonNull Long placeId;
	private @NonNull String placeName;
	private @NonNull String placeMapx;
	private @NonNull String placeMapy;
	private String memo;
	private @NonNull String visitDate;
	private @NonNull Integer visitOrder;
}
