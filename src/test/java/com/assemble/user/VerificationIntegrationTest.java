package com.assemble.user;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.fixture.UserFixture;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Verification Integration Test")
@CustomIntegrationTest
public class VerificationIntegrationTest {

    private final String basePath = "/assemble/";

    @LocalServerPort
    private int port;

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
}
