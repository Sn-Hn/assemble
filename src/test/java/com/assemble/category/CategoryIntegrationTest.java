package com.assemble.category;

import com.assemble.annotation.CustomIntegrationTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@CustomIntegrationTest
@DisplayName("Category Integration Test")
public class CategoryIntegrationTest {
    private final String basePath = "/assemble/";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 카테고리_목록_전체_조회() {
        given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .get("category")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.size()", equalTo(1))
                .log().all();
    }
}
