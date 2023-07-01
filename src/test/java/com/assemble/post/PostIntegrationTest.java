package com.assemble.post;

import com.assemble.commons.response.ApiResult;
import com.assemble.file.fixture.FileFixture;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.fixture.PostFixture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.hamcrest.Matchers;
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
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Post Integration Test")
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    void 게시글_작성_프로필_사진_X() throws FileNotFoundException {
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
                .body("success", equalTo(true),
                        "response.title", equalTo(postCreationRequest.getTitle()))
                .log().all();

    }
}
