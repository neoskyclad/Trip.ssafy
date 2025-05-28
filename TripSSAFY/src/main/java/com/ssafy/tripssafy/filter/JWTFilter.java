package com.ssafy.tripssafy.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripssafy.exception.ErrorCode;
import com.ssafy.tripssafy.exception.ErrorResponse;
import com.ssafy.tripssafy.service.CustomUserDetailService;
import com.ssafy.tripssafy.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    
    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
    	String authorizationHeader = request.getHeader("Authorization");
    	//JWT 헤더가 있을 경우
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			//JWT 유효성 검증
			try {
				jwtUtil.validateToken(token);
				
				String username = jwtUtil.extractUsername(token);
				//유저와 토큰 일치 시 userDetails 생성
				UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

				if (userDetails != null) {
					//UserDetails, Password, Role -> 접근 권한 인증 Token 생성
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

					//현재 Request의 Security Context에 접근 권한 설정
					SecurityContextHolder.getContext()
						.setAuthentication(usernamePasswordAuthenticationToken);
					log.info("컨텍스트 설정됨: {}", userDetails.getUsername());
				}
			}  catch (SignatureException | SecurityException | MalformedJwtException e) {
	            log.info("Invalid JWT Token", e);
	            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
	            return;
	        } catch (ExpiredJwtException e) {
	            log.info("Expired JWT Token", e);
	            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
	            return;
	        } catch (UnsupportedJwtException e) {
	            log.info("Unsupported JWT Token", e);
	            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
	            return;
	        } catch (IllegalArgumentException e) {
	            log.info("JWT claims string is empty.", e);
	            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
	            return;
	        }
			
		}

		filterChain.doFilter(request, response); //다음 필터로 넘김
    }
    
    /**
     * 에러 응답을 보내는 메소드
     * ControllerAdvice 스타일로 일관된 에러 포맷 유지
     */
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

    	ObjectMapper objectMapper = new ObjectMapper();
    	
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(responseBody);
    }
}