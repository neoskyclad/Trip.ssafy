package com.ssafy.tripssafy.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TripRoom {
	private @NonNull Long id;
	private @NonNull String title;
	private @NonNull LocalDate startDate;
	private @NonNull LocalDate endDate;
	private @NonNull LocalDateTime createdAt;
}
