package com.assemble.post.service;

import com.assemble.commons.base.UserContext;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.fixture.PostLikeFixture;
import com.assemble.post.repository.PostLikeRepository;
import com.assemble.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("PostLikeService")
@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @InjectMocks
    private PostLikeService postLikeService;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 게시글_좋아요() {
        // given
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.empty());
        given(postLikeRepository.save(any())).willReturn(PostLikeFixture.좋아요_객체());
        given(postRepository.findById(any())).willReturn(Optional.of(PostFixture.게시글()));
        given(userContext.getUserId()).willReturn(1L);
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_요청();

        // when
        boolean isLike = postLikeService.likePost(postLikeRequest);

        // then
        assertThat(isLike).isTrue();
    }

    @Test
    void 게시글_좋아요_취소() {
        // given
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.of(PostLikeFixture.좋아요_객체()));
        given(postRepository.findById(any())).willReturn(Optional.of(PostFixture.게시글()));
        given(userContext.getUserId()).willReturn(1L);
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_취소_요청();

        // when
        boolean isLike = postLikeService.cancelLikePost(postLikeRequest.getPostId());

        // then
        assertThat(isLike).isTrue();
    }

    @Test
    void 이미_좋아요_한_게시글() {
        // given
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_요청();
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.of(PostLikeFixture.좋아요_객체()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        boolean aleadyLikeByUser = postLikeService.isAleadyLikeByUser(postLikeRequest);

        // then
        assertThat(aleadyLikeByUser).isTrue();
    }

    @Test
    void 좋아요_하지_않은_게시글() {
        // given
        PostLikeRequest postLikeRequest = PostLikeFixture.게시글_좋아요_요청();
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.empty());
        given(userContext.getUserId()).willReturn(1L);

        // when
        boolean aleadyLikeByUser = postLikeService.isAleadyLikeByUser(postLikeRequest);

        // then
        assertThat(aleadyLikeByUser).isFalse();
    }
}