package com.assemble.mock;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.service.JwtService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpecificationSpy {

    private static TokenSpy tokenSpy;

    public static RequestSpecification setTokenRestAssuredSpec(JwtService jwtService) {
        tokenSpy = new TokenSpy(jwtService);
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + tokenSpy.generateAccessToken())
                .addCookie("RefreshToken", tokenSpy.generateRefreshToken())
                .build();
    }
}
