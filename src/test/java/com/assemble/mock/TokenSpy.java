package com.assemble.mock;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.service.JwtService;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;

public class TokenSpy {

    private static JwtService jwtService;

    public TokenSpy(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public static String generateAccessToken() {
        User user = UserFixture.회원();
        return jwtService.issueAccessToken(user.getUserId(), user.getEmail().getValue());
    }

    public static String generateRefreshToken() {
        User user = UserFixture.회원();
        return jwtService.issueRefreshToken(user.getUserId(), user.getEmail().getValue());
    }
}
