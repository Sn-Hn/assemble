package com.assemble.user;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.file.fixture.FileFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.*;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("User Integration Test")
@CustomIntegrationTest
public class UserIntegrationTest {

    private final String basePath = "/assemble/";

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

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
    void 회원가입_성공_프로필_사진_X() {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(signupRequest, Map.class))
                .log().all()
        .when()
                .post("signup")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(201),
                        "error", equalTo(null),
                        "response.email", equalTo(signupRequest.getEmail()),
                        "response.profile", nullValue())
                .log().all();
    }

    @Test
    void 회원가입_성공_프로필_사진_O() throws FileNotFoundException {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_두번째_회원();
        File file = FileFixture.File_생성();
        given()
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
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.email", equalTo(signupRequest.getEmail()),
                        "response.profile", notNullValue())
                .log().all();
    }

    @Test
    void 특정_회원_조회() {
        Long userId = 1L;
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("userId", userId)
                .log().all()
        .when()
                .get("user/{userId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.userId", equalTo(userId.intValue()))
                .log().all();
    }

    @Test
    void 회원_탈퇴() {
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpecFromWithdrawUser(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .delete("user/withdrawal")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", equalTo(true))
                .log().all();
    }
}
