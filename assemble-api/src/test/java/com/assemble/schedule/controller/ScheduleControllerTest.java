package com.assemble.schedule.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.entity.Schedule;
import com.assemble.schedule.fixture.ScheduleFixture;
import com.assemble.schedule.service.ScheduleService;
import com.assemble.util.MultiValueMapConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = ScheduleController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 일정_등록() throws Exception {
        ScheduleCreationRequest scheduleCreationRequest = ScheduleFixture.일정_생성_요청();
        given(scheduleService.registerSchdule(any())).willReturn(ScheduleFixture.일정_9월());

        ResultActions perform = mockMvc.perform(post("/schedule")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(scheduleCreationRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("schedule/creation",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("title").description("일정 제목"),
                        fieldWithPath("content").description("일정 내용"),
                        fieldWithPath("date").description("날짜")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.id").description("일정 ID"),
                        fieldWithPath("response.title").description("일정 제목"),
                        fieldWithPath("response.content").description("일정 내용"),
                        fieldWithPath("response.date").description("날짜"),
                        fieldWithPath("response.writeDate").description("작성일")
                ))
        );
    }

    @Test
    void 일정_목록_연월_조회() throws Exception {
        String yearAndMonth = "2023-09";
        ScheduleYearAndMonthRequest request = new ScheduleYearAndMonthRequest(yearAndMonth);
        given(scheduleService.findSchedulesByYearAndMonth(any())).willReturn(List.of(ScheduleFixture.일정_9월()));

        ResultActions perform = mockMvc.perform(get("/schedule")
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(MultiValueMapConverter.convert(objectMapper, request)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("schedule/list",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestParameters(
                        parameterWithName("yearAndMonth").description("특정 연월")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.schedules[].day").description("특정 월의 일"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].id").description("일정 ID"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].title").description("일정 제목"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].day").description("특정 월의 일"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].date").description("일정 날짜"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.schedules[].schedulesOfMonth[].writeDate").description("일정 작성일")
                ))
        );
    }

    @Test
    void 일정_상세_조회() throws Exception {
        Long id = 1L;
        given(scheduleService.findScheduleById(anyLong())).willReturn(ScheduleFixture.일정_9월());

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule/{scheduleId}", id)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("schedule/detail",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                pathParameters(
                        parameterWithName("scheduleId").description("일정 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.id").description("일정 ID"),
                        fieldWithPath("response.title").description("일정 제목"),
                        fieldWithPath("response.content").description("일정 내용"),
                        fieldWithPath("response.date").description("날짜"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writeDate").description("일정 작성일")
                ))
        );
    }

    @Test
    void 일정_변경() throws Exception {
        ModifiedScheduleRequest modifiedScheduleRequest = ScheduleFixture.일정_변경_요청();
        Schedule schedule = ScheduleFixture.일정_9월();
        schedule.modify(modifiedScheduleRequest.getTitle(), modifiedScheduleRequest.getContent());
        given(scheduleService.modifySchedule(any())).willReturn(schedule);

        ResultActions perform = mockMvc.perform(put("/schedule")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(modifiedScheduleRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("schedule/modification",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("id").description("일정 ID"),
                        fieldWithPath("title").description("일정 제목"),
                        fieldWithPath("content").description("일정 내용")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.id").description("일정 ID"),
                        fieldWithPath("response.title").description("일정 제목"),
                        fieldWithPath("response.content").description("일정 내용"),
                        fieldWithPath("response.date").description("날짜"),
                        fieldWithPath("response.writeDate").description("작성일"),
                        fieldWithPath("response.writerNickname").description("작성자")
                ))
        );
    }

    @Test
    void 일정_삭제() throws Exception {
        Long id = 2L;
        given(scheduleService.deleteSchedule(anyLong())).willReturn(true);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/schedule/{scheduleId}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("schedule/delete",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                pathParameters(
                        parameterWithName("scheduleId").description("일정 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("삭제 성공 여부")
                ))
        );
    }
}