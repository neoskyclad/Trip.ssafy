package com.ssafy.tripssafy.model.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomExpenseResponse {
	private String placeName;
	private Integer amount;
	private LocalDate visitDate;
}
