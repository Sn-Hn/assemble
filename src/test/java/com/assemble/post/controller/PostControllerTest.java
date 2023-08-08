package com.assemble.post.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.converter.PageableConverter;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.fixture.PageableFixture;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.service.PostService;
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
@WebMvcTest(controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 모임_등록() throws Exception {
        PostCreationRequest postCreationRequest = PostFixture.게시글_작성_사진_X();
        given(postService.createPost(any())).willReturn(PostFixture.게시글());
        ResultActions perform = mockMvc.perform(post("/post")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(postCreationRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.title").value(postCreationRequest.getTitle()))
                .andExpect(jsonPath("$.response.contents").value(postCreationRequest.getContents()));

        perform.andDo(document("/post/creation",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("contents").description("내용"),
                        fieldWithPath("categoryId").description("카테고리 ID"),
                        fieldWithPath("personnelNumber").description("모집 인원"),
                        fieldWithPath("expectedPeriod").description("예상 기간")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.title").description("제목"),
                        fieldWithPath("response.contents").description("내용"),
                        fieldWithPath("response.categoryId").description("카테고리 ID"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likeCount").description("좋아요 수"),
                        fieldWithPath("response.personnelNumber").description("모집 인원"),
                        fieldWithPath("response.expectedPeriod").description("예상 기간"),
                        fieldWithPath("response.postProfile").description("게시글 프로필 사진 목록"),
                        fieldWithPath("response.postStatus").description("모임 상태 (모집 중, 모집 완료)")
                ))
        );
    }

    @Test
    void 모임_목록_조회() throws Exception {
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_제목_검색();
        given(postService.getPosts(any(), any())).willReturn(new PageImpl<>(List.of(PostFixture.게시글())));

        ResultActions perform = mockMvc.perform(get("/post")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(MultiValueMapConverter.convert(objectMapper, postSearchRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("/post/list",
                requestParameters(
                        parameterWithName("searchQuery").description("검색어 ex) 제목"),
                        parameterWithName("searchBy").description("검색할 주제 ex) title, writer, contents"),
                        parameterWithName("categoryId").description("검색할 카테고리 ID")
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
                ))
        );
    }

    @Test
    void 모임_상세_조회() throws Exception {
        Long postId = 1L;
        given(postService.getPost(any())).willReturn(PostFixture.게시글());
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.postId").value(postId));

        perform.andDo(document("/post/detail",
                pathParameters(
                        parameterWithName("postId").description("모임 Id")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.postId").description("모임 Id"),
                        fieldWithPath("response.title").description("제목"),
                        fieldWithPath("response.contents").description("내용"),
                        fieldWithPath("response.categoryName").description("카테고리 ID"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.writerProfileImages").description("작성자 프로필 사진 목록"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likes").description("좋아요 수"),
                        fieldWithPath("response.likeStatus").description("좋아요 여부"),
                        fieldWithPath("response.personnelNumber").description("모집 인원"),
                        fieldWithPath("response.expectedPeriod").description("예상 기간"),
                        fieldWithPath("response.commentCount").description("댓글 수"),
                        fieldWithPath("response.comments").description("댓글"),
                        fieldWithPath("response.createdTime").description("작성일"),
                        fieldWithPath("response.postProfileImages").description("게시글 프로필 사진 목록"),
                        fieldWithPath("response.postStatus").description("모임 상태 (모집 중, 모집 완료)")
                ))
        );
    }

    @Test
    void 모임_수정() throws Exception {
        ModifiedPostRequest modifiedPostRequest = PostFixture.게시글_수정();
        Post post = PostFixture.게시글();
        post.modifyPost(modifiedPostRequest);
        given(postService.modifyPost(any())).willReturn(post);
        ResultActions perform = mockMvc.perform(patch("/post")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(modifiedPostRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.postId").value(modifiedPostRequest.getPostId()))
                .andExpect(jsonPath("$.response.title").value(modifiedPostRequest.getTitle()))
                .andExpect(jsonPath("$.response.contents").value(modifiedPostRequest.getContents()));

        perform.andDo(document("/post/modification",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("postId").description("모임 ID"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("contents").description("내용"),
                        fieldWithPath("categoryId").description("카테고리 ID"),
                        fieldWithPath("personnelNumber").description("모집 인원"),
                        fieldWithPath("expectedPeriod").description("예상 기간"),
                        fieldWithPath("postStatus").description("모임 상태 (모집 중, 모집 완료)")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.postId").description("모임 Id"),
                        fieldWithPath("response.title").description("제목"),
                        fieldWithPath("response.contents").description("내용"),
                        fieldWithPath("response.categoryName").description("카테고리 ID"),
                        fieldWithPath("response.writerNickname").description("작성자 닉네임"),
                        fieldWithPath("response.writerId").description("작성자 ID"),
                        fieldWithPath("response.writerProfileImages").description("작성자 프로필 사진 목록"),
                        fieldWithPath("response.hits").description("조회 수"),
                        fieldWithPath("response.likes").description("좋아요 수"),
                        fieldWithPath("response.likeStatus").description("좋아요 여부"),
                        fieldWithPath("response.personnelNumber").description("모집 인원"),
                        fieldWithPath("response.expectedPeriod").description("예상 기간"),
                        fieldWithPath("response.commentCount").description("댓글 수"),
                        fieldWithPath("response.comments").description("댓글"),
                        fieldWithPath("response.createdTime").description("작성일"),
                        fieldWithPath("response.postProfileImages").description("게시글 프로필 사진 목록"),
                        fieldWithPath("response.postStatus").description("모임 상태 (모집 중, 모집 완료)")
                ))
        );
    }

    @Test
    void 모임_삭제() throws Exception {
        Long postId = 2L;

        given(postService.deletePost(any())).willReturn(true);
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/{postId}", postId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/post/delete",
                pathParameters(
                        parameterWithName("postId").description("모임 Id")
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
        given(postService.getPostsByUser(anyLong(), any())).willReturn((new PageImpl<>(List.of(PostFixture.게시글()))));

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/post/user/{userId}", userId)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.content[0].writerId").value(userId));

        perform.andDo(document("/post/user",
                pathParameters(
                        parameterWithName("userId").description("특정 회원 ID")
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
                ))
        );
    }
}