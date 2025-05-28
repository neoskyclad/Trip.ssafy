package com.ssafy.tripssafy.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final int status;
	private final String code;
	private final String message;
	
	public ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getHttpStatus().value();
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}
	
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}
}
