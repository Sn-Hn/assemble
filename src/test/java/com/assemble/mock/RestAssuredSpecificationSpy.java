package com.assemble.mock;

import com.assemble.auth.service.JwtService;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpecificationSpy {

    private static TokenSpy tokenSpy;

    public static RequestSpecification setTokenRestAssuredSpec(JwtService jwtService) {
        return getRequestSpecification(jwtService, UserFixture.회원());
    }

    public static RequestSpecification setTokenRestAssuredSpecAdmin(JwtService jwtService) {
        return getRequestSpecification(jwtService, UserFixture.관리자());
    }

    public static RequestSpecification setTokenRestAssuredSpecFromWithdrawUser(JwtService jwtService) {
        return getRequestSpecification(jwtService, UserFixture.탈퇴할_회원());
    }

    private static RequestSpecification getRequestSpecification(JwtService jwtService, User mockUser) {
        tokenSpy = TokenSpy.generateTokenSpy(jwtService);
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + tokenSpy.generateAccessToken(mockUser))
                .addCookie("RefreshToken", tokenSpy.generateRefreshToken(mockUser))
                .build();
    }
}
