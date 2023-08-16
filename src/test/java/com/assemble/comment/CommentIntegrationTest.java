package com.assemble.comment;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.fixture.CommentFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Comment Integration Test")
@CustomIntegrationTest
public class CommentIntegrationTest {

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
    void 댓글_생성() {
        CommentCreationRequest commentCreationRequest = CommentFixture.댓글_생성_요청();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentCreationRequest)
                .log().all()
        .when()
                .post("comment")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(201),
                        "response.contents", equalTo(commentCreationRequest.getContents()))
                .log().all();
    }

    @Test
    void 댓글_수정() {
        ModifiedCommentRequest modifiedCommentRequest = CommentFixture.댓글_수정_요청();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(modifiedCommentRequest)
                .log().all()
        .when()
                .put("comment")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.contents", equalTo(modifiedCommentRequest.getContents()))
                .log().all();
    }

    @Test
    void 댓글_삭제() {
        Long commentId = 2L;

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("commentId", commentId)
                .log().all()
        .when()
                .delete("comment/{commentId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 특정_유저_댓글_조회() {
        Long userId = 1L;

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("userId", userId)
                .log().all()
        .when()
                .get("comment/user/{userId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", notNullValue())
                .log().all();
    }
}
