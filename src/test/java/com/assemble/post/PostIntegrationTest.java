package com.assemble.post;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.file.fixture.FileFixture;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.fixture.PostFixture;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Post Integration Test")
@CustomIntegrationTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class PostIntegrationTest {

    private final String basePath = "/assemble/";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private RestAssuredConfig config = config()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
            .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"))
            .httpClient(HttpClientConfig.httpClientConfig().httpMultipartMode(HttpMultipartMode.BROWSER_COMPATIBLE));

    @Test
    void 게시글_작성_프로필_사진_X() {
        PostCreationRequest postCreationRequest = PostFixture.게시글_작성_사진_X();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(postCreationRequest, Map.class))
                .log().all()
        .when()
                .post("post")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.title", equalTo(postCreationRequest.getTitle()))
                .log().all();
    }

    @Test
    void 게시글_작성_프로필_사진_O() throws FileNotFoundException {
        PostCreationRequest postCreationRequest = PostFixture.게시글_작성_사진_X();
        File file = FileFixture.File_생성();
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParams(objectMapper.convertValue(postCreationRequest, Map.class))
                .multiPart("multipartFile", file, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .log().all()
        .when()
                .config(config)
                .post("post")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.title", equalTo(postCreationRequest.getTitle()))
                .log().all();
    }

    @Test
    void 게시글_목록_조회_제목_검색() {
        Pageable pageable = PageRequest.of(0, 16);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_제목_검색();

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(postSearchRequest, Map.class))
                .queryParams(objectMapper.convertValue(pageable, Map.class))
                .log().all()
        .when()
                .config(config)
                .get("post")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.content[0].title", containsString(postSearchRequest.getSearchQuery()))
                .log().all();
    }

    @Test
    void 게시글_목록_조회_내용_검색() {
        Pageable pageable = PageRequest.of(0, 16);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_내용_검색();

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(postSearchRequest, Map.class))
                .queryParams(objectMapper.convertValue(pageable, Map.class))
                .log().all()
        .when()
                .config(config)
                .get("post")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.content[0].contents", containsString(postSearchRequest.getSearchQuery()))
                .log().all();
    }

    @Test
    void 게시글_상세_조회() {
        Long id = 1L;

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("postId", id)
                .log().all()
        .when()
                .config(config)
                .get("post/{postId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.postId", equalTo(id.intValue()))
                .log().all();
    }

    @Test
    void 게시글_수정() {
        ModifiedPostRequest modifiedPostRequest = PostFixture.게시글_수정();

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(modifiedPostRequest, Map.class))
                .log().all()
        .when()
                .config(config)
                .patch("post")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response.postId", equalTo(modifiedPostRequest.getPostId().intValue()),
                        "response.title", equalTo(modifiedPostRequest.getTitle()),
                        "response.contents", equalTo(modifiedPostRequest.getContents()))
                .log().all();
    }
    @Test
    void 게시글_삭제() {
        Long id = 2L;

        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("postId", id)
                .log().all()
        .when()
                .config(config)
                .delete("post/{postId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "response", is(true))
                .log().all();
    }
}
