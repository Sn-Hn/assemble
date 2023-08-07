package com.assemble.post;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.fixture.PostLikeFixture;
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

@DisplayName("Post Like Integration Test")
@CustomIntegrationTest
public class PostLikeIntegrationTest {

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
    void 게시글_좋아요() {
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_요청();

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postLikeRequest)
                .log().all()
        .when()
                .post("post/like")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "status", equalTo(201),
                        "response", is(true))
                .log().all();
    }

    @Test
    void 게시글_좋아요_취소() {
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_취소_요청();

        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("postId", postLikeRequest.getPostId())
                .log().all()
        .when()
                .delete("post/like/{postId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", is(true))
                .log().all();
    }
}
