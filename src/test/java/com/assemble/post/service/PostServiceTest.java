package com.assemble.post.service;

import com.assemble.file.fixture.FileFixture;
import com.assemble.file.service.FileService;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("Post")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private FileService fileService;

    @Test
    void 게시글_작성() {
        // given
        PostCreationRequest postCreationRequest = PostFixture.게시글_작성_사진_X();
        MultipartFile file = null;
        given(postRepository.save(any()))
                .willReturn(PostFixture.게시글());
        given(fileService.uploadFile(any(), any()))
                .willReturn(null);

        // when
        PostCreationResponse response = postService.createPost(postCreationRequest, file);

        // then
        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(postCreationRequest.getTitle()),
                () -> assertThat(response.getContents()).isEqualTo(postCreationRequest.getContents()),
                () -> assertThat(response.getCategory()).isEqualTo(postCreationRequest.getCategory()),
                () -> assertThat(response.getWriterId()).isEqualTo(postCreationRequest.getWriter())

        );
    }
}