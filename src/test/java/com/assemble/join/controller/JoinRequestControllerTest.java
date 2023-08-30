package com.assemble.join.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.fixture.PageableFixture;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.fixture.JoinRequestFixture;
import com.assemble.join.service.JoinRequestService;
import com.assemble.util.MultiValueMapConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = JoinRequestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
public class JoinRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JoinRequestService joinRequestService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Test
    void 모임_가입_신청() throws Exception {
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        given(joinRequestService.requestJoinToAssemble(any())).willReturn(JoinRequestFixture.정상_신청_회원());

        ResultActions perform = mockMvc.perform(post("/join")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(joinRequestDto)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.postId").value(joinRequestDto.getPostId()))
                .andExpect(jsonPath("$.response.status").value(JoinRequestStatus.REQUEST.toString()));

        perform
                .andDo(document("/join",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("postId").description("모임 ID"),
                                fieldWithPath("joinRequestMessage").description("가입 신청 메시지")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.joinRequestId").description("가입 신청 ID"),
                                fieldWithPath("response.postId").description("모임 ID"),
                                fieldWithPath("response.userId").description("모임 가입 신청자 ID"),
                                fieldWithPath("response.status").description("가입 신청 상태"),
                                fieldWithPath("response.message").description("가입 신청 메시지")
                        )
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"APPROVAL", "REJECT", "BLOCK"})
    void 모임_가입_처리(String status) throws Exception {
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        given(joinRequestService.responseJoinFromAssemble(any())).willReturn(JoinRequestFixture.가입_처리_응답(JoinRequestStatus.valueOf(status), null));

        ResultActions perform = mockMvc.perform(put("/join")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(joinRequestAnswer)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.status").value(status));

        perform
                .andDo(document("/join/answer",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("joinRequestId").description("모임 가입 신청 ID"),
                                fieldWithPath("status").description("가입 신청 응답 상태"),
                                fieldWithPath("message").description("가입 신청 거절 메시지")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.joinRequestId").description("가입 신청 ID"),
                                fieldWithPath("response.postId").description("모임 ID"),
                                fieldWithPath("response.userId").description("모임 가입 신청자 ID"),
                                fieldWithPath("response.status").description("가입 신청 상태"),
                                fieldWithPath("response.message").description("가입 신청 메시지")
                        )
                ));
    }

    @Test
    void 모임_가입_취소() throws Exception {
        Long postId = 2L;
        given(joinRequestService.cancelJoinOfAssemble(anyLong())).willReturn(true);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.put("/join/cancel/{postId}", postId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform
                .andDo(document("/join/cancel",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response").description("모임 가입 신청 취소 여부")
                        )
                ));
    }

    @Test
    void 모임_가입_신청_목록_조회() throws Exception {
        Long postId = 1L;
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given(joinRequestService.getJoinRequests(anyLong(), any()))
                .willReturn(new PageImpl<>(List.of(JoinRequestFixture.정상_신청_회원())));

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/join/{postId}", postId)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, pageableConverter)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.content").isNotEmpty());

        perform
                .andDo(document("/join/cancel",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
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
                                fieldWithPath("response.content[0].joinRequestId").description("가입 신청 ID"),
                                fieldWithPath("response.content[0].postId").description("모임 ID"),
                                fieldWithPath("response.content[0].userId").description("모임 가입 신청자 ID"),
                                fieldWithPath("response.content[0].status").description("가입 신청 상태"),
                                fieldWithPath("response.content[0].message").description("가입 신청 메시지"),
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
}