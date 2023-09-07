package com.assemble.activity.controller;

import com.assemble.activity.fixture.ActivityFixture;
import com.assemble.activity.service.ActivityService;
import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.fixture.PageableFixture;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.util.MultiValueMapConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Activity Controller Test")
@WebMvcTest(value = ActivityController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class ActivityControllerTest {

    @MockBean
    private ActivityService activityService;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public WebMvcConfig webMvcConfig;

    @MockBean
    public TokenInformationInterceptor tokenInformationInterceptor;

    @Test
    void 회원이_활동_중인_모임_목록_조회() throws Exception {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given(activityService.getActiveAssembles(any()))
                .willReturn(new PageImpl<>(List.of(MeetingFixture.모임())));

        ResultActions perform = this.mockMvc.perform(get("/activity/meeting")
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, pageableConverter)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.content").isNotEmpty());

        perform
                .andDo(document("activity/list",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("size").description("페이지 별 수"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("sort").description("정렬")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.content[0].meetingId").description("모임 ID"),
                                fieldWithPath("response.content[0].name").description("모임 이름"),
                                fieldWithPath("response.content[0].description").description("모임 설명"),
                                fieldWithPath("response.content[0].categoryName").description("카테고리 이름"),
                                fieldWithPath("response.content[0].writerNickname").description("작성자 닉네임"),
                                fieldWithPath("response.content[0].writerId").description("작성자 ID"),
                                fieldWithPath("response.content[0].writerProfileImages").description("작성자 프로필 이미지 목록"),
                                fieldWithPath("response.content[0].hits").description("조회 수"),
                                fieldWithPath("response.content[0].likes").description("좋아요 수"),
                                fieldWithPath("response.content[0].likeStatus").description("좋아요 여부"),
                                fieldWithPath("response.content[0].commentCount").description("댓글 수"),
                                fieldWithPath("response.content[0].activityUserCount").description("모임 활동 중인 인원"),
                                fieldWithPath("response.content[0].createdTime").description("모임 생성일"),
                                fieldWithPath("response.content[0].meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                                fieldWithPath("response.pageable").description("pageable"),
                                fieldWithPath("response.last").description("last"),
                                fieldWithPath("response.totalPages").description("총 페이지 수"),
                                fieldWithPath("response.totalElements").description("총 개수"),
                                fieldWithPath("response.size").description("size"),
                                fieldWithPath("response.number").description("number"),
                                fieldWithPath("response.sort.empty").description("sort.empty"),
                                fieldWithPath("response.sort.sorted").description("sort.sorted"),
                                fieldWithPath("response.sort.unsorted").description("sort.unsorted"),
                                fieldWithPath("response.numberOfElements").description("numberOfElements"),
                                fieldWithPath("response.first").description("first"),
                                fieldWithPath("response.empty").description("empty")
                        )
                ));
    }

    @Test
    void 모임에_활동_중인_회원_목록_조회() throws Exception {
        Long meetingId = 1L;
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given(activityService.getJoinUserOfAssemble(anyLong(), any()))
                .willReturn(new PageImpl<>(List.of(ActivityFixture.특정_모임_활동_중인_회원())));

        ResultActions perform = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/activity/user/{meetingId}", meetingId)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, pageableConverter)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.content").isNotEmpty());

        perform
                .andDo(document("activity/user",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("size").description("페이지 별 수"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("sort").description("정렬")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.content[0].meetingId").description("모임 ID"),
                                fieldWithPath("response.content[0].userId").description("회원 ID"),
                                fieldWithPath("response.content[0].nickname").description("회원 닉네임"),
                                fieldWithPath("response.content[0].profile").description("회원 프로필"),
                                fieldWithPath("response.content[0].host").description("모임장 여부"),
                                fieldWithPath("response.pageable").description("pageable"),
                                fieldWithPath("response.last").description("last"),
                                fieldWithPath("response.totalPages").description("총 페이지 수"),
                                fieldWithPath("response.totalElements").description("총 개수"),
                                fieldWithPath("response.size").description("size"),
                                fieldWithPath("response.number").description("number"),
                                fieldWithPath("response.sort.empty").description("sort.empty"),
                                fieldWithPath("response.sort.sorted").description("sort.sorted"),
                                fieldWithPath("response.sort.unsorted").description("sort.unsorted"),
                                fieldWithPath("response.numberOfElements").description("numberOfElements"),
                                fieldWithPath("response.first").description("first"),
                                fieldWithPath("response.empty").description("empty")
                        )
                ));
    }

    @Test
    void 모임_탈퇴() throws Exception {
        Long meetingId = 2L;

        given(activityService.withdrawJoinAssemble(anyLong())).willReturn(true);
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.put("/activity/withdrawal/{meetingId}", meetingId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("activity/withdrawal",
                pathParameters(
                        parameterWithName("meetingId").description("모임 Id")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("모임 탈퇴 성공 여부")
                ))
        );
    }
}