package com.assemble.meeting;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.meeting.dto.request.MeetingLikeRequest;
import com.assemble.meeting.fixture.MeetingLikeFixture;
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

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Meeting Like Integration Test")
@CustomIntegrationTest
public class MeetingLikeIntegrationTest {

    private final String basePath = "/assemble";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private RestAssuredConfig config = config()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
            .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"))
            .httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE));

    @Test
    void 모임_좋아요() {
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_요청();

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(meetingLikeRequest)
                .log().all()
        .when()
                .post("meeting/like")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(201),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 모임_좋아요_취소() {
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_취소_요청();

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingLikeRequest.getMeetingId())
                .log().all()
        .when()
                .delete("meeting/like/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 좋아요_한_모임_목록_조회() {
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .get("meeting/like")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", notNullValue())
                .log().all();
    }
}
