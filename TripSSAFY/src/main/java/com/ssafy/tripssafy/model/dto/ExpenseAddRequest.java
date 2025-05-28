package com.ssafy.tripssafy.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExpenseAddRequest {
	@NotNull
	private String nickname;
	
	@NotNull
	private Long tripPlaceId;
	
	@NotNull
	private Integer amount;
}
