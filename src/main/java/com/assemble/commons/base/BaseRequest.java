package com.assemble.commons.base;

public abstract class BaseRequest {
    private static Long userId;

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
