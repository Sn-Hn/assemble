package com.assemble.comment.controller;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.comment.fixture.CommentFixture;
import com.assemble.comment.service.CommentService;
import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.fixture.PageableFixture;
import com.assemble.util.MultiValueMapConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 댓글_작성() throws Exception {
        CommentCreationRequest commentCreationRequest = CommentFixture.댓글_생성_요청();
        given(commentService.createComment(any())).willReturn(CommentFixture.댓글_조회());

        ResultActions perform = mockMvc.perform(post("/comment")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentCreationRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.contents").value(commentCreationRequest.getContents()));

        perform
                .andDo(document("/comment/creation",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("postId").description("모임 ID"),
                                fieldWithPath("contents").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.postId").description("모임 ID"),
                                fieldWithPath("response.userId").description("댓글 작성자 ID"),
                                fieldWithPath("response.commentId").description("댓글 ID"),
                                fieldWithPath("response.contents").description("댓글 내용"),
                                fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                                fieldWithPath("response.writeDate").description("작성일"),
                                fieldWithPath("response.profile").description("작성자 프로필 사진 목록")
                        )
                ));
    }

    @Test
    void 댓글_수정() throws Exception {
        ModifiedCommentRequest modifiedCommentRequest = CommentFixture.댓글_수정_요청();
        Comment comment = CommentFixture.댓글_조회();
        comment.modifyComment(modifiedCommentRequest);
        given(commentService.modifyComment(any())).willReturn(comment);

        ResultActions perform = mockMvc.perform(put("/comment")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(modifiedCommentRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.commentId").value(modifiedCommentRequest.getCommentId()))
                .andExpect(jsonPath("$.response.contents").value(modifiedCommentRequest.getContents()));

        perform
                .andDo(document("/comment/modification",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("commentId").description("모임 ID"),
                                fieldWithPath("contents").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response.postId").description("모임 ID"),
                                fieldWithPath("response.userId").description("댓글 작성자 ID"),
                                fieldWithPath("response.commentId").description("댓글 ID"),
                                fieldWithPath("response.contents").description("댓글 내용"),
                                fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                                fieldWithPath("response.writeDate").description("작성일"),
                                fieldWithPath("response.profile").description("작성자 프로필 사진 목록")
                        )
                ));
    }

    @Test
    void 댓글_삭제() throws Exception {
        Long commentId = 1L;
        given(commentService.deleteComment(any())).willReturn(true);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/comment/{commentId}", commentId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform
                .andDo(document("/comment/delete",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("상태값"),
                                fieldWithPath("error").description("에러 내용"),
                                fieldWithPath("response").description("삭제 성공 여부")
                        )
                ));
    }

    @Test
    void 특정_회원이_작성한_댓글_조회() throws Exception {
        Long userId = 1L;
        PageableConverter pageableConverter = PageableFixture.pageableConverter_생성();
        given(commentService.getCommentsByUser(anyLong(), any())).willReturn(new PageImpl<>(List.of(CommentFixture.댓글_조회())));

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/comment/user/{userId}", userId)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, pageableConverter)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content[0].userId").value(userId));

        perform
                .andDo(document("/comment/user",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer AccessToken")
                        ),
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
                                fieldWithPath("response.content[0].postId").description("모임 ID"),
                                fieldWithPath("response.content[0].postTitle").description("모임 제목"),
                                fieldWithPath("response.content[0].userId").description("댓글 작성자 ID"),
                                fieldWithPath("response.content[0].commentId").description("댓글 ID"),
                                fieldWithPath("response.content[0].contents").description("댓글 내용"),
                                fieldWithPath("response.content[0].writerNickname").description("작성자 닉네임"),
                                fieldWithPath("response.content[0].writeDate").description("작성일"),
                                fieldWithPath("response.content[0].profile").description("작성자 프로필 사진 목록"),
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