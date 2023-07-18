package com.assemble.post.repository;

import com.assemble.annotation.CustomRepositoryTest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("PostRepository")
@CustomRepositoryTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void 게시글_목록_제목_검색() {
        // given
        Pageable pageable = PageRequest.of(0, 12);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_제목_검색();

        // when
        Page<Post> allPostBySearch = postRepository.findAllBySearch(postSearchRequest, pageable);
        Post searchPost = allPostBySearch.get().filter(post -> post.getTitle().getValue().contains(postSearchRequest.getSearchQuery()))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allPostBySearch).isNotEmpty(),
                () -> assertThat(searchPost).isNotNull(),
                () -> assertThat(searchPost.getTitle().getValue()).contains(postSearchRequest.getSearchQuery())
        );
    }

    @Test
    void 게시글_목록_내용_검색() {
        // given
        Pageable pageable = PageRequest.of(0, 12);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_내용_검색();

        // when
        Page<Post> allPostBySearch = postRepository.findAllBySearch(postSearchRequest, pageable);
        Post searchPost = allPostBySearch.get().filter(post -> post.getContents().getValue().contains(postSearchRequest.getSearchQuery()))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allPostBySearch).isNotEmpty(),
                () -> assertThat(searchPost).isNotNull(),
                () -> assertThat(searchPost.getContents().getValue()).contains(postSearchRequest.getSearchQuery())
        );
    }

    @Test
    void 게시글_목록_작성자_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 12);
        PostSearchRequest postSearchRequest = PostFixture.게시글_목록_작성자_검색();

        // when
        Page<Post> allPostBySearch = postRepository.findAllBySearch(postSearchRequest, pageable);
        Post searchPost = allPostBySearch.get().filter(post -> post.getUser().getUserId().equals(Long.valueOf(postSearchRequest.getSearchQuery())))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allPostBySearch).isNotEmpty(),
                () -> assertThat(searchPost).isNotNull(),
                () -> assertThat(searchPost.getUser().getUserId()).isEqualTo(Long.valueOf(postSearchRequest.getSearchQuery()))
        );
    }

    @Test
    void 게시글_삭제_검증() {
        // given
        Post post = PostFixture.게시글();

        // when
        postRepository.delete(post);
        boolean isDeletedPost = postRepository.findById(post.getPostId()).isPresent();

        // then
        assertThat(isDeletedPost).isFalse();
    }
}