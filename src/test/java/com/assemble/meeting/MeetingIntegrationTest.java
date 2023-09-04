package com.assemble.meeting;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.fixture.PageableFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.fixture.MeetingFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Post Integration Test")
@CustomIntegrationTest
public class MeetingIntegrationTest {

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

    private RestAssuredConfig config = config()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
            .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"))
            .httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE));

    @Test
    void 모임_생성_프로필_사진_X() {
        MeetingCreationRequest meetingCreationRequest = MeetingFixture.모임_작성_사진_X();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(meetingCreationRequest)
                .log().all()
        .when()
                .post("meeting")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(201),
                        "response.name", equalTo(meetingCreationRequest.getName()))
                .log().all();
    }

//    @Test
//    void 모임_작성_프로필_사진_O() throws FileNotFoundException {
//        PostCreationRequest postCreationRequest = PostFixture.모임_작성_사진_X();
//        File file = FileFixture.File_생성();
//        given()
//                .spec(RestAssuredSpecificationSpy.getRestAssuredSpec(jwtProvider))
//                .basePath(basePath)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                .queryParams(objectMapper.convertValue(postCreationRequest, Map.class))
//                .multiPart("multipartFile", file, MediaType.APPLICATION_OCTET_STREAM_VALUE)
//                .log().all()
//        .when()
//                .config(config)
//                .post("post")
//        .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("success", is(true),
//                        "response.name", equalTo(postCreationRequest.getName()))
//                .log().all();
//    }

    @Test
    void 모임_목록_조회_제목_검색() {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_이름_검색();

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(meetingSearchRequest, Map.class))
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .config(config)
                .get("meeting")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.content[0].name", containsString(meetingSearchRequest.getSearchQuery()))
                .log().all();
    }

    @Test
    void 모임_목록_조회_내용_검색() {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_설명_검색();

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(meetingSearchRequest, Map.class))
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .config(config)
                .get("meeting")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.content[0].description", containsString(meetingSearchRequest.getSearchQuery()))
                .log().all();
    }

    @Test
    void 모임_상세_조회() {
        Long id = 1L;

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", id)
                .log().all()
        .when()
                .config(config)
                .get("meeting/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.meetingId", equalTo(id.intValue()))
                .log().all();
    }

    @Test
    void 모임_수정() {
        ModifiedMeetingRequest modifiedMeetingRequest = MeetingFixture.모임_수정();

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(modifiedMeetingRequest)
                .log().all()
        .when()
                .config(config)
                .put("meeting")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.meetingId", equalTo(modifiedMeetingRequest.getMeetingId().intValue()),
                        "response.name", equalTo(modifiedMeetingRequest.getName()),
                        "response.description", equalTo(modifiedMeetingRequest.getDescription()))
                .log().all();
    }

    @Test
    void 모임_삭제() {
        Long meetingId = 2L;

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("meetingId", meetingId)
                .log().all()
        .when()
                .config(config)
                .delete("meeting/{meetingId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 특정_회원이_작성한_모임_목록_조회() {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        Long userId = 1L;

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("userId", userId)
                .queryParams(objectMapper.convertValue(pageableConverter, Map.class))
                .log().all()
        .when()
                .config(config)
                .get("meeting/user/{userId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.content[0].writerId", equalTo(userId.intValue()))
                .log().all();
    }
}
