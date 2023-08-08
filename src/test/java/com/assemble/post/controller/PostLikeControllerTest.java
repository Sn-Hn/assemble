package com.assemble.post.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.post.dto.request.PostLikeRequest;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
}