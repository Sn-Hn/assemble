package com.assemble.category;

import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.fixture.CategoryFixture;
import com.assemble.mock.RestAssuredSpecificationSpy;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtService jwtService;

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
                        "response.size()", greaterThan(1))
                .log().all();
    }

    @Test
    void 카테고리_등록() {
        CategoryCreationRequest categoryCreationRequest = CategoryFixture.카테고리_등록();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpecAdmin(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(categoryCreationRequest)
                .log().all()
        .when()
                .post("category")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.categoryName", equalTo(categoryCreationRequest.getCategoryName()))
                .log().all();
    }

    @Test
    void 카테고리_수정() {
        ModifiedCategoryRequest modifiedCategoryRequest = CategoryFixture.카테고리_수정();
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpecAdmin(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(modifiedCategoryRequest)
                .log().all()
        .when()
                .put("category")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response.categoryName", equalTo(modifiedCategoryRequest.getCategoryName()))
                .log().all();
    }

    @Test
    void 카테고리_삭제() {
        given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpecAdmin(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("categoryId", 2L)
                .log().all()
        .when()
                .delete("category/{categoryId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("success", is(true),
                        "error", equalTo(null),
                        "response", is(true))
                .log().all();
    }

}
