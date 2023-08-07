package com.assemble.commons;

public class TokenFixture {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String BEARER = "Bearer ";

    public static String AccessToken_생성() {
        return BEARER + ACCESS_TOKEN;
    }
    public static String RefreshToken_생성() {
        return REFRESH_TOKEN;
    }

}
