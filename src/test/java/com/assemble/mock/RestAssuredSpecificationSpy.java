package com.assemble.mock;

import com.assemble.auth.service.JwtService;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpecificationSpy {

    private static TokenSpy tokenSpy;

    public static RequestSpecification setTokenRestAssuredSpec(JwtService jwtService) {
        tokenSpy = TokenSpy.generateTokenSpy(jwtService);
        User user = UserFixture.회원();
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + tokenSpy.generateAccessToken(user))
                .addCookie("RefreshToken", tokenSpy.generateRefreshToken(user))
                .build();
    }

    public static RequestSpecification setTokenRestAssuredSpecFromWithdrawUser(JwtService jwtService) {
        tokenSpy = TokenSpy.generateTokenSpy(jwtService);
        User user = UserFixture.탈퇴할_회원();
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + tokenSpy.generateAccessToken(user))
                .addCookie("RefreshToken", tokenSpy.generateRefreshToken(user))
                .build();
    }
}
