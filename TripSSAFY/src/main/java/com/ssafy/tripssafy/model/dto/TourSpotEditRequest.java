package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TourSpotEditRequest {
	private @NonNull Long id;	// 해당 spot의 id
}
