package com.assemble.post.service;

import com.assemble.commons.base.BaseRequest;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("PostService")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeService postLikeService;

    @Test
    void 게시글_작성() {
        // given
        PostCreationRequest postCreationRequest = PostFixture.게시글_작성_사진_X();
        given(postRepository.save(any()))
                .willReturn(PostFixture.게시글());

        // when
        PostCreationResponse response = postService.createPost(postCreationRequest);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(postCreationRequest.getTitle()),
                () -> assertThat(response.getContents()).isEqualTo(postCreationRequest.getContents()),
                () -> assertThat(response.getCategoryId()).isEqualTo(postCreationRequest.getCategoryId()),
                () -> assertThat(response.getWriterId()).isEqualTo(postCreationRequest.getWriter())

        );
    }

    @Test
    void 게시글_목록_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 12);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_제목_검색();
        List<Post> postList = List.of(PostFixture.게시글());
        given(postRepository.findAllBySearch(any(), any(), anyLong())).willReturn(new PageImpl<>(postList, pageable, pageable.getPageSize()));


        // when
        Page<Post> posts = postService.getPosts(postSearchRequest, pageable);

        // then
        assertAll(
                () -> assertThat(posts).isNotNull(),
                () -> assertThat(posts).hasSizeGreaterThan(0)
        );
    }

    @Test
    void 게시글_상세_조회() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(PostFixture.게시글()));
        given(postLikeService.isAleadyLikeByUser(any())).willReturn(false);
        BaseRequest.setUserId(1L);

        // when
        Post post = postService.getPost(1L);

        // then
        assertAll(
                () -> assertThat(post).isNotNull(),
                () -> assertThat(post.getPostId()).isEqualTo(1L),
                () -> assertThat(post.getHits()).isEqualTo(3L)
        );
    }

    @Test
    void 게시글_수정() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(PostFixture.게시글()));
        ModifiedPostRequest modifiedPostRequest = PostFixture.게시글_수정();

        // when
        Post post = postService.modifyPost(modifiedPostRequest);

        // then
        assertAll(
                () -> assertThat(post).isNotNull(),
                () -> assertThat(post.getTitle().getValue()).isEqualTo(modifiedPostRequest.getTitle()),
                () -> assertThat(post.getContents().getValue()).isEqualTo(modifiedPostRequest.getContents())
        );
    }

    @Test
    void 게시글_삭제() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(PostFixture.게시글())).willReturn(null);
        Post post = PostFixture.게시글();

        // when
        boolean isDeletedPost = postService.deletePost(post.getPostId());

        // then
        assertThat(isDeletedPost).isTrue();
    }

    @Test
    void 특정_회원이_작성한_게시글_조회() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 12);
        List<Post> posts = List.of(PostFixture.게시글());
        given(postRepository.countByUserId(any())).willReturn(1L);
        given(postRepository.findAllByUserId(anyLong(), any(), anyLong())).willReturn(new PageImpl<>(posts, pageable, pageable.getPageSize()));

        // when
        Page<Post> postsByUser = postService.getPostsByUser(userId, pageable);

        // then
        assertAll(
                () -> assertThat(postsByUser).isNotEmpty(),
                () -> assertThat(postsByUser.get().count()).isEqualTo(posts.size()),
                () -> assertThat(postsByUser.get().findFirst().get()
                        .getUser()
                        .getUserId()).isEqualTo(userId)
        );
    }
}