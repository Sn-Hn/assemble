package com.assemble.user;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.ValidationPasswordRequest;
import com.assemble.user.dto.request.ValidationUserRequest;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Validation Integration Test")
@CustomIntegrationTest
public class ValidationIntegrationTest {

    private final String basePath = "/assemble/";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private RestAssuredConfig config = RestAssured.config()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
            .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"))
            .httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE));

    @Test
    void 이메일_중복_검증_성공() {
        EmailRequest emailRequest = UserFixture.중복_아닌_이메일();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("emailRequest", emailRequest.getEmail())
                .log().all()
        .when()
                .get("email/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", is(false))
                .log().all()
                .extract();
    }

    @Test
    void 이메일_중복_검증_실패() {
        EmailRequest emailRequest = UserFixture.중복_이메일();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("emailRequest", emailRequest.getEmail())
                .log().all()
        .when()
                .get("email/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", is(true))
                .log().all();

    }

    @Test
    void 닉네임_중복_검증_성공() {
        NicknameRequest nicknameRequest = UserFixture.중복_아닌_닉네임();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("nicknameRequest", nicknameRequest.getNickname())
                .log().all()
        .when()
                .get("nickname/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", is(false))
                .log().all();
    }

    @Test
    void 닉네임_중복_검증_실패() {
        NicknameRequest nicknameRequest = UserFixture.중복_닉네임();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("nicknameRequest", nicknameRequest.getNickname())
                .log().all()
        .when()
                .get("nickname/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 계정_확인() {
        ValidationUserRequest validationUserRequest = new ValidationUserRequest("test00@gmail.com", "tester00", "01000000000", "20000101");
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(validationUserRequest)
                .log().all()
        .when()
                .post("user/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.token", notNullValue())
                .log().all();
    }

    @Test
    void 비밀번호_확인() {
        ValidationPasswordRequest validationPasswordRequest = new ValidationPasswordRequest("password2!");
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpecAdmin(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(validationPasswordRequest)
                .log().all()
        .when()
                .post("password/validation")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.token", notNullValue())
                .log().all();
    }
}
