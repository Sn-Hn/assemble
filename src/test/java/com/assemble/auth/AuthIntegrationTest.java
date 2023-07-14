package com.assemble.auth;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Auth Integration Test")
@CustomIntegrationTest
public class AuthIntegrationTest {

    private final String basePath = "/assemble/";

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 로그인_성공() {
        LoginRequest loginRequest = UserFixture.로그인_성공_회원();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .log().all()
        .when()
                .post("authentication")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.email", equalTo(loginRequest.getEmail()))
                .log().all();
    }

    @Test
    void 로그인_실패() {
        LoginRequest loginRequest = UserFixture.로그인_실패_회원();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .log().all()
        .when()
                .post("authentication")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("success", is(false),
                        "response", equalTo(null),
                        "error.status", equalTo(404),
                        "status", equalTo(404))
                .log().all();
    }
}