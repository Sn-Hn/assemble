package com.assemble.commons.base;

public abstract class BaseRequest {
    // default -> 로그인하지 않은 회원
    private static Long userId = -1L;

    private static String email;

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
}
