package com.ssafy.tripssafy.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ssafy.tripssafy.model.dto.CustomUserDetails;

//현재 로그인된 사용자의 정보를 가져오는 서비스입니다.
@Service
public class IdentityService {

    /**
     * 현재 인증된 사용자의 principal 객체를 반환합니다.
     */
    public Object getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getPrincipal();
    }

    /**
     * 현재 로그인된 사용자 ID 반환 (UserDetails 구현체 기준)
     */
    public Long getCurrentUserId() {
        Object principal = getCurrentPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser().getId(); // 또는 getUsername() 등 필요에 맞게
        }
        return null;
    }

    /**
     * 현재 로그인된 사용자 이름 반환 (옵션)
     */
    public String getCurrentUsername() {
        Object principal = getCurrentPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }
    
    public String getCurrentUserPassword() {
    	Object principal = getCurrentPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getPassword();
        }
        return null;
    }
}
