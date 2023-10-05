package com.assemble.auth.domain;

public enum JwtType {
    ACCESS_TOKEN("AccessToken"),
    REFRESH_TOKEN("RefreshToken");

    private String code;

    JwtType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
