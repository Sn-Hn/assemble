package com.assemble.activity;

import com.assemble.activity.dto.request.DismissUserRequest;
import com.assemble.activity.fixture.ActivityFixture;
import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.response.ApiResult;
import com.assemble.fixture.PageableFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.util.IntegrationTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Activity Integration Test")
@CustomIntegrationTest
public class ActivityIntegrationTest {
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

    @Test
    void 회원이_활동_중인_모임_목록_조회() {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .get("activity/meeting")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue())
                .log().all();
    }

    @Test
    void 모임에_활동_중인_회원_목록_조회() {
        Long meetingId = 1L;
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingId)
                .log().all()
        .when()
                .get("activity/user/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue())
                .log().all();
    }

    @Test
    void 모임_탈퇴() {
        Long meetingId = 3L;
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingId)
                .log().all()
        .when()
                .put("activity/withdrawal/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 모임_강제_퇴장() {
        DismissUserRequest dismissUserRequest = ActivityFixture.회원_강퇴_요청();
        ExtractableResponse<Response> putResponse = IntegrationTestUtil.put("/activity/dismissal", dismissUserRequest);

        ApiResult result = putResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result.getResponse()).isEqualTo(true),
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(200)
        );

    }
}
