package com.assemble.post.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.fixture.PageableFixture;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.fixture.PostLikeFixture;
import com.assemble.post.service.PostLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostLikeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class PostLikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostLikeService postLikeService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 좋아요() throws Exception {
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_요청();
        given(postLikeService.likePost(any())).willReturn(true);

        ResultActions perform = mockMvc.perform(post("/post/like")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(postLikeRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/post/like",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("postId").description("모임 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("좋아요 성공 여부")
                )
        ));
    }

    @Test
    void 좋아요_취소() throws Exception {
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_취소_요청();
        given(postLikeService.cancelLikePost(any())).willReturn(true);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/like/{postId}", postLikeRequest.getPostId())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(postLikeRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/post/like/cancel",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                pathParameters(
                        parameterWithName("postId").description("모임 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("좋아요 성공 여부")
                )
        ));
    }

    @Test
    void 좋아요_한_모임_목록_조회() throws Exception {
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        List<Post> posts = List.of(PostFixture.게시글());
        given(postLikeService.getPostsByLike(any())).willReturn(new PageImpl<>(posts));

        ResultActions perform = mockMvc.perform(get("/post/like")
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(pageable)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("/post/like",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.content[0].postId").description("모인 번호"),
                        fieldWithPath("response.content[0].title").description("모임 제목"),
                        fieldWithPath("response.content[0].contents").description("내용"),
                        fieldWithPath("response.content[0].categoryName").description("카테고리 이름"),
                        fieldWithPath("response.content[0].writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.content[0].writerId").description("작성자 ID"),
                        fieldWithPath("response.content[0].writerProfileImages").description("작성자 프로필 이미지 목록"),
                        fieldWithPath("response.content[0].hits").description("조회 수"),
                        fieldWithPath("response.content[0].likes").description("좋아요 수"),
                        fieldWithPath("response.content[0].likeStatus").description("좋아요 여부"),
                        fieldWithPath("response.content[0].commentCount").description("댓글 수"),
                        fieldWithPath("response.content[0].personnelNumber").description("모집 인원"),
                        fieldWithPath("response.content[0].expectedPeriod").description("예상 기간"),
                        fieldWithPath("response.content[0].postProfileImages").description("게시글 프로필 사진 목록"),
                        fieldWithPath("response.content[0].postStatus").description("모임 상태 (모집 중, 모집 완료)"),
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