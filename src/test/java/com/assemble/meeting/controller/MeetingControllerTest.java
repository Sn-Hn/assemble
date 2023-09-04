package com.assemble.meeting.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.fixture.PageableFixture;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.meeting.service.MeetingService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = MeetingController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 모임_등록() throws Exception {
        MeetingCreationRequest meetingCreationRequest = MeetingFixture.모임_작성_사진_X();
        given(meetingService.createPost(any())).willReturn(MeetingFixture.모임());
        ResultActions perform = mockMvc.perform(post("/meeting")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(meetingCreationRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.name").value(meetingCreationRequest.getName()))
                .andExpect(jsonPath("$.response.description").value(meetingCreationRequest.getDescription()));

        perform.andDo(document("/meeting/creation",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("name").description("모임 이름"),
                        fieldWithPath("description").description("모임 설명"),
                        fieldWithPath("categoryId").description("카테고리 ID"),
                        fieldWithPath("zipCode").description("우편번호"),
                        fieldWithPath("roadNameAddress").description("도로명 주소"),
                        fieldWithPath("lotNumberAddress").description("지번 주소"),
                        fieldWithPath("detailAddress").description("상세 주소")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.name").description("모임 이름"),
                        fieldWithPath("response.description").description("모임 설명"),
                        fieldWithPath("response.categoryId").description("카테고리 ID"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likeCount").description("좋아요 수"),
                        fieldWithPath("response.activityUserCount").description("모임 활동 중인 인원"),
                        fieldWithPath("response.meetingProfile").description("모임 프로필 사진 목록"),
                        fieldWithPath("response.meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                        fieldWithPath("response.roadNameAddress").description("도로명 주소"),
                        fieldWithPath("response.detailAddress").description("상세 주소")
                ))
        );
    }

    @Test
    void 모임_목록_조회() throws Exception {
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_이름_검색();
        given(meetingService.getMeetings(any(), any())).willReturn(new PageImpl<>(List.of(MeetingFixture.모임())));

        ResultActions perform = mockMvc.perform(get("/meeting")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(MultiValueMapConverter.convert(objectMapper, meetingSearchRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("/meeting/list",
                requestParameters(
                        parameterWithName("searchQuery").description("검색어 ex) 제목"),
                        parameterWithName("searchBy").description("검색할 주제 ex) name, writer, description"),
                        parameterWithName("categoryId").description("검색할 카테고리 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.content[0].meetingId").description("모임 번호"),
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
                        fieldWithPath("response.content[0].meetingProfileImages").description("모임 프로필 사진 목록"),
                        fieldWithPath("response.content[0].meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                        fieldWithPath("response.content[0].roadNameAddress").description("도로명 주소"),
                        fieldWithPath("response.content[0].detailAddress").description("상세 주소"),
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
                ))
        );
    }

    @Test
    void 모임_상세_조회() throws Exception {
        Long meetingId = 1L;
        given(meetingService.getMeeting(any())).willReturn(MeetingFixture.모임());
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/meeting/{meetingId}", meetingId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.meetingId").value(meetingId));

        perform.andDo(document("/meeting/detail",
                pathParameters(
                        parameterWithName("meetingId").description("모임 Id")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.meetingId").description("모임 Id"),
                        fieldWithPath("response.name").description("모임 이름"),
                        fieldWithPath("response.description").description("모임 설명"),
                        fieldWithPath("response.categoryName").description("카테고리 ID"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.writerProfileImages").description("작성자 프로필 사진 목록"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likes").description("좋아요 수"),
                        fieldWithPath("response.likeStatus").description("좋아요 여부"),
                        fieldWithPath("response.activityUserCount").description("모임 활동 중인 인원"),
                        fieldWithPath("response.commentCount").description("댓글 수"),
                        fieldWithPath("response.comments").description("댓글"),
                        fieldWithPath("response.createdTime").description("작성일"),
                        fieldWithPath("response.meetingProfileImages").description("모임 프로필 사진 목록"),
                        fieldWithPath("response.meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                        fieldWithPath("response.roadNameAddress").description("도로명 주소"),
                        fieldWithPath("response.detailAddress").description("상세 주소")
                ))
        );
    }

    @Test
    void 모임_수정() throws Exception {
        ModifiedMeetingRequest modifiedMeetingRequest = MeetingFixture.모임_수정();
        Meeting meeting = MeetingFixture.모임();
        meeting.modifyPost(modifiedMeetingRequest);
        given(meetingService.modifyPost(any())).willReturn(meeting);
        ResultActions perform = mockMvc.perform(put("/meeting")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(modifiedMeetingRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.meetingId").value(modifiedMeetingRequest.getMeetingId()))
                .andExpect(jsonPath("$.response.name").value(modifiedMeetingRequest.getName()))
                .andExpect(jsonPath("$.response.description").value(modifiedMeetingRequest.getDescription()));

        perform.andDo(document("/meeting/modification",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("meetingId").description("모임 ID"),
                        fieldWithPath("name").description("모임 이름"),
                        fieldWithPath("description").description("모임 설명"),
                        fieldWithPath("categoryId").description("카테고리 ID"),
                        fieldWithPath("meetingStatus").description("모임 상태 (모집 중, 모집 완료)")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.meetingId").description("모임 Id"),
                        fieldWithPath("response.name").description("모임 이름"),
                        fieldWithPath("response.description").description("모임 설명"),
                        fieldWithPath("response.categoryName").description("카테고리 이름"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.writerProfileImages").description("작성자 프로필 사진 목록"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likes").description("좋아요 수"),
                        fieldWithPath("response.likeStatus").description("좋아요 여부"),
                        fieldWithPath("response.activityUserCount").description("모임 활동 중인 인원"),
                        fieldWithPath("response.commentCount").description("댓글 수"),
                        fieldWithPath("response.comments").description("댓글"),
                        fieldWithPath("response.createdTime").description("작성일"),
                        fieldWithPath("response.meetingProfileImages").description("모임 프로필 사진 목록"),
                        fieldWithPath("response.meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                        fieldWithPath("response.roadNameAddress").description("도로명 주소"),
                        fieldWithPath("response.detailAddress").description("상세 주소")
                ))
        );
    }

    @Test
    void 모임_삭제() throws Exception {
        Long meetingId = 2L;

        given(meetingService.deletePost(any())).willReturn(true);
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/meeting/{meetingId}", meetingId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/meeting/delete",
                pathParameters(
                        parameterWithName("meetingId").description("모임 Id")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("삭제 성공 여부")
                ))
        );
    }

    @Test
    void 특정_회원이_작성한_모임_조회() throws Exception {
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        Long userId = 1L;
        given(meetingService.getMeetingsByUser(anyLong(), any())).willReturn((new PageImpl<>(List.of(MeetingFixture.모임()))));

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/meeting/user/{userId}", userId)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, pageableConverter)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.content[0].writerId").value(userId));

        perform.andDo(document("/meeting/user",
                pathParameters(
                        parameterWithName("userId").description("특정 회원 ID")
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
                        fieldWithPath("response.content[0].meetingId").description("모인 번호"),
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
                        fieldWithPath("response.content[0].meetingProfileImages").description("모임 프로필 사진 목록"),
                        fieldWithPath("response.content[0].meetingStatus").description("모임 상태 (모집 중, 모집 완료)"),
                        fieldWithPath("response.content[0].roadNameAddress").description("도로명 주소"),
                        fieldWithPath("response.content[0].detailAddress").description("상세 주소"),
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
                ))
        );
    }
}