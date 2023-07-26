package com.assemble.mock;

import com.assemble.auth.service.JwtService;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;

public class TokenSpy {

    private static TokenSpy tokenSpy;
    private static JwtService jwtService;

    private TokenSpy(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public static String generateAccessToken(User user) {
        return jwtService.issueAccessToken(user.getUserId(), user.getEmail().getValue());
    }

    public static String generateRefreshToken(User user) {
        return jwtService.issueRefreshToken(user.getUserId(), user.getEmail().getValue());
    }

    public static TokenSpy generateTokenSpy(JwtService jwtService) {
        if (tokenSpy == null) {
            tokenSpy = new TokenSpy(jwtService);
        }

        return tokenSpy;
    }
}
