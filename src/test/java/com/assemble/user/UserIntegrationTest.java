package com.assemble.user;

import com.assemble.commons.response.ApiResult;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Integration Test")
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

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
    void 로그인_성공() throws IOException {
        LoginRequest loginRequest = UserFixture.로그인_성공_회원();
        ExtractableResponse<Response> authenticationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .log().all()
                .when()
                .post("authentication")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = authenticationResponse.jsonPath().getObject(".", ApiResult.class);
        Map<String, String> response = (HashMap<String, String>) result.getResponse();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isNotNull(),
                () -> assertThat(response.get("email")).isEqualTo(loginRequest.getEmail())
        );
    }

    @Test
    void 로그인_실패() {
        LoginRequest loginRequest = UserFixture.로그인_실패_회원();
        ExtractableResponse<Response> authenticationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .log().all()
                .when()
                .post("authentication")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract();

        ApiResult result = authenticationResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isFalse(),
                () -> assertThat(result.getResponse()).isNull(),
                () -> assertThat(result.getError()).isNotNull(),
                () -> assertThat(result.getStatus()).isEqualTo(404)
        );
    }

    @Test
    void 회원가입_성공_프로필_사진_X() {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        ExtractableResponse<Response> signupResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(signupRequest, Map.class))
                .log().all()
                .when()
                .post("signup")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = signupResponse.jsonPath().getObject(".", ApiResult.class);
        Map<String, String> response = (HashMap<String, String>) result.getResponse();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isNotNull(),
                () -> assertThat(response.get("email")).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(response.get("name")).isEqualTo(signupRequest.getName())
        );
    }
}
