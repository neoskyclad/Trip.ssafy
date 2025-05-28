package com.ssafy.tripssafy.model.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TripRoomAddRequest {
	
	private Long id;	//삽입 후 들어감
	
	@NotBlank(message = "방 이름은 필수 값입니다.")
	private String title;
	
	private @NonNull LocalDate start_date;
	
	private @NonNull LocalDate end_date;
	
	private List<String> invitedUsernames;
	
}
