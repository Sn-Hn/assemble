package com.assemble.auth.domain;

public enum JwtType {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken");

    private String code;

    JwtType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
