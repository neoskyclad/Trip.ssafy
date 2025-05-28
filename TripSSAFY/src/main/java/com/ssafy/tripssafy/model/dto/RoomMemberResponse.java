package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class RoomMemberResponse{
	private @NonNull String nickname;
	private String profileImg;
}