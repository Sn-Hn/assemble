package com.assemble.user;

import com.assemble.commons.response.ApiResult;
import com.assemble.file.fixture.FileFixture;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Integration Test")
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
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

    private RestAssuredConfig config = RestAssured.config()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
            .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"))
            .httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE));

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
        Map<String, Object> response = (HashMap<String, Object>) result.getResponse();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isNotNull(),
                () -> assertThat(response.get("email")).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(response.get("name")).isEqualTo(signupRequest.getName()),
                () -> assertThat(((List<String>) response.get("profile")).size()).isEqualTo(0)
        );
    }

    @Test
    void 회원가입_성공_프로필_사진_O() {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        File file = FileFixture.File_생성();
        ExtractableResponse<Response> signupResponse = given()
                .config(config)
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParams(objectMapper.convertValue(signupRequest, Map.class))
                .multiPart("profileImage", file)
                .urlEncodingEnabled(true)
                .log().all()
                .when()
                .post("signup")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = signupResponse.jsonPath().getObject(".", ApiResult.class);
        Map<String, Object> response = (HashMap<String, Object>) result.getResponse();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isNotNull(),
                () -> assertThat(response.get("email")).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(response.get("name")).isEqualTo(signupRequest.getName()),
                () -> assertThat(response.get("profile")).isNotNull()
        );
    }

    @Test
    void 이메일_중복_검증_성공() {
        EmailRequest emailRequest = UserFixture.중복_아닌_이메일();
        ExtractableResponse<Response> emailValidationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("emailRequest", emailRequest.getEmail())
                .log().all()
                .when()
                .get("email/validation")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = emailValidationResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isEqualTo(true),
                () -> assertThat(result.getError()).isNull()
        );
    }

    @Test
    void 이메일_중복_검증_실패() {
        EmailRequest emailRequest = UserFixture.중복_이메일();
        ExtractableResponse<Response> emailValidationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("emailRequest", emailRequest.getEmail())
                .log().all()
                .when()
                .get("email/validation")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = emailValidationResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isEqualTo(false),
                () -> assertThat(result.getError()).isNull()
        );
    }

    @Test
    void 닉네임_중복_검증_성공() {
        NicknameRequest nicknameRequest = UserFixture.중복_아닌_닉네임();
        ExtractableResponse<Response> emailValidationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("nicknameRequest", nicknameRequest.getNickname())
                .log().all()
                .when()
                .get("nickname/validation")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = emailValidationResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isEqualTo(true),
                () -> assertThat(result.getError()).isNull()
        );
    }

    @Test
    void 닉네임_중복_검증_실패() {
        NicknameRequest nicknameRequest = UserFixture.중복_닉네임();
        ExtractableResponse<Response> emailValidationResponse = given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .formParam("nicknameRequest", nicknameRequest.getNickname())
                .log().all()
                .when()
                .get("nickname/validation")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();

        ApiResult result = emailValidationResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getResponse()).isEqualTo(false),
                () -> assertThat(result.getError()).isNull()
        );
    }
}
