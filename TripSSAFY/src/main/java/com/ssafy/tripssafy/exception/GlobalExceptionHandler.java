package com.ssafy.tripssafy.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * DTO의 Validation 오류 전역 처리 -> 400 BadRequest와 errorMsg를 반환하도록 함
	 * ex) SignupRequest의 아이디, 비밀번호, 닉네임 조건 오류
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("errorMsg", errorMessage));
    }
    
    /**
     * RequestParam 생략 에러
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String message = String.format("Missing required request parameter: '%s'", name);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errorMsg", message));
    }
    
    /**
     * 참조하는 foreign key id의 entity가 없을 때 에러
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleValidationException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(Map.of("errorMsg", ex.getMessage()));
    }
    
    /**
     * 접근 권한이 없는 url 호출
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
    }
}
