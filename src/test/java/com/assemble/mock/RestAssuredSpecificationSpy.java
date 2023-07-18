package com.assemble.mock;

import com.assemble.auth.domain.JwtProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class RestAssuredSpecificationSpy {

    private static AccessTokenSpy accessTokenSpy;

    public static RequestSpecification getRestAssuredSpec(JwtProvider jwtProvider) {
        accessTokenSpy = new AccessTokenSpy(jwtProvider);
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + accessTokenSpy.generateAccessToken())
                .build();
    }
}
