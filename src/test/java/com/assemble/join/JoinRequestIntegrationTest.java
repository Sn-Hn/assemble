package com.assemble.join;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.fixture.PageableFixture;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.fixture.JoinRequestFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("JoinRequest Integration Test")
@CustomIntegrationTest
public class JoinRequestIntegrationTest {
    private final String basePath = "/assemble/";

    @LocalServerPort
    int port;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 모임_가입_신청() {
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(joinRequestDto)
                .log().all()
        .when()
                .post("join")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue())
                .log().all();
    }

    @Test
    void 모임_가입_신청_승인() {
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_승인();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(joinRequestAnswer)
                .log().all()
        .when()
                .put("join")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue(),
                        "response.status", equalTo(joinRequestAnswer.getStatus()))
                .log().all();
    }

    @Test
    void 모임_가입_신청_취소() {
        Long meetingId = 2L;
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingId)
                .log().all()
        .when()
                .put("join/cancel/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 모임_가입_신청_목록_조회() {
        Long meetingId = 1L;
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingId)
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .get("join/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue())
                .log().all();
    }

    @Test
    void 가입_신청한_모임_조회() {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .get("join")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(200),
                        "response", notNullValue())
                .log().all();
    }
}
