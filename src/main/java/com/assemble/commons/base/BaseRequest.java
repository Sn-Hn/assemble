package com.assemble.commons.base;

import com.assemble.user.domain.UserRole;

public abstract class BaseRequest {
    // default -> 로그인하지 않은 회원
    private static Long userId = -1L;

    private static String email;

    private static UserRole role;

    public static Long getUserId() {
        return userId;
    }

    public static String getEmail() {
        return email;
    }

    public static void setUserId(Long userId) {
        BaseRequest.userId = userId;
    }

    public static void setEmail(String email) {
        BaseRequest.email = email;
    }

    public static UserRole getRole() {
        return role;
    }

    public static void setRole(UserRole role) {
        BaseRequest.role = role;
    }
}
