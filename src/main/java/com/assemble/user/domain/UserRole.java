package com.assemble.user.domain;

public enum UserRole {
    USER("user"),
    VIP("vip"),
    ADMIN("admin");

    private String code;

    UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
