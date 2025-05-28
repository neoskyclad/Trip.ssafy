package com.ssafy.tripssafy.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameResultAddRequest {
	
	@NotNull
	private Long tripPlaceId;
	
	@NotNull
	private String resultURL;	//html 타입의 게임 결과 전송
}
