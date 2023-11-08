package com.assemble.schedule;


import com.assemble.annotation.CustomIntegrationTest;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.response.ApiResult;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.dto.response.ScheduleResponse;
import com.assemble.schedule.dto.response.SchedulesResponse;
import com.assemble.schedule.fixture.ScheduleFixture;
import com.assemble.util.IntegrationTestUtil;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Meeting Integration Test")
@CustomIntegrationTest
public class ScheduleIntegrationTest {
    private final String basePath = "/assemble";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 일정_등록_성공() {
        ScheduleCreationRequest scheduleCreationRequest = ScheduleFixture.일정_생성_요청();
        ExtractableResponse<Response> postResponse = IntegrationTestUtil.post("/schedule", scheduleCreationRequest);
        ApiResult result = postResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED.value())
        );
    }

    /**
     *  조회한 연월은 시작날짜, 종료날짜 사이에 존재해야 함
     *  ex) 2023-10으로 검색했을 경우,
     *  startDate: 2023-05-05, endDate: 2023-12-12 안에 포함되어야 함
     */
    @ParameterizedTest
    @ValueSource(strings = {"2023-09", "2023-10"})
    void 일정_목록_연월_조회_검증(String yearAndMonth) {
        ScheduleYearAndMonthRequest request = new ScheduleYearAndMonthRequest(yearAndMonth);
        ExtractableResponse<Response> getResponse = IntegrationTestUtil.getQueryParamWithJWT("/schedule", request);
        ApiResult result = getResponse.jsonPath().getObject(".", ApiResult.class);
        SchedulesResponse response = getResponse.jsonPath().getObject("response", SchedulesResponse.class);

        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.getSchedules().get(0).getSchedulesOfMonth().stream()
                        .findAny().get().getDate()).contains(yearAndMonth)
        );
    }

    @Test
    void 일정_목록_상세_조회_검증() {
        Long id = 1L;

        ExtractableResponse<Response> getResponse = IntegrationTestUtil.getWithJWT("/schedule/" + id);
        ApiResult result = getResponse.jsonPath().getObject(".", ApiResult.class);
        ScheduleResponse response = getResponse.jsonPath().getObject("response", ScheduleResponse.class);

        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.getId()).isEqualTo(id)
        );
    }

    @Test
    void 일정_변경_검증() {
        ModifiedScheduleRequest modifiedScheduleRequest = ScheduleFixture.일정_변경_요청();

        ExtractableResponse<Response> putResponse = IntegrationTestUtil.put("/schedule", modifiedScheduleRequest);
        ApiResult result = putResponse.jsonPath().getObject(".", ApiResult.class);
        ScheduleResponse response = putResponse.jsonPath().getObject("response", ScheduleResponse.class);

        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.getId()).isEqualTo(modifiedScheduleRequest.getId()),
                () -> assertThat(response.getTitle()).isEqualTo(modifiedScheduleRequest.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(modifiedScheduleRequest.getContent())
        );
    }
    
    @Test
    void 일정_삭제_검증() {
        Long id = 2L;

        ExtractableResponse<Response> deleteResponse = IntegrationTestUtil.delete("/schedule/" + id);
        ApiResult result = deleteResponse.jsonPath().getObject(".", ApiResult.class);

        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.getResponse()).isEqualTo(true)
        );
    }
}
