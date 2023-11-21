package com.assemble.util;

import com.assemble.auth.service.JwtService;
import com.assemble.mock.RestAssuredSpecificationSpy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.restassured.RestAssured.given;

@Component
public class IntegrationTestUtil {
    private static final String basePath = "/assemble";

    private static JwtService jwtService;
    private static ObjectMapper objectMapper;

    public IntegrationTestUtil(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    public static ExtractableResponse<Response> getQueryParam(String path, Object queryParam) {
        return given()
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(queryParam, Map.class))
                .log().all()
        .when()
                .get(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getQueryParamWithJWT(String path, Object queryParam) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(objectMapper.convertValue(queryParam, Map.class))
                .log().all()
        .when()
                .get(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getWithJWT(String path) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .get(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .post(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object object) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(object)
                .log().all()
        .when()
                .post(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object object) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(object)
                .log().all()
        .when()
                .put(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return given()
                .spec(RestAssuredSpecificationSpy.setTokenRestAssuredSpec(jwtService))
                .basePath(basePath)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
        .when()
                .delete(path)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }
}
