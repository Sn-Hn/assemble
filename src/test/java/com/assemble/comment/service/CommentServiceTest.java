package com.assemble.comment.service;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.comment.fixture.CommentFixture;
import com.assemble.comment.repository.CommentRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("CommentService")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 댓글_생성() {
        // given
        CommentCreationRequest commentCreationRequest = CommentFixture.댓글_생성_요청();
        Comment comment = CommentFixture.댓글_조회();
        given(commentRepository.save(any())).willReturn(comment);
        given(userContext.getUserId()).willReturn(1L);

        // when
        Comment savedComment = commentService.createComment(commentCreationRequest);

        //then
        assertThat(savedComment.getCommentId()).isEqualTo(comment.getCommentId());
    }

    @Test
    void 댓글_수정() {
        // given
        ModifiedCommentRequest modifiedCommentRequest = CommentFixture.댓글_수정_요청();
        given(commentRepository.findById(any())).willReturn(Optional.of(CommentFixture.댓글_조회()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        Comment comment = commentService.modifyComment(modifiedCommentRequest);

        // then
        assertAll(
                () -> assertThat(comment.getCommentId()).isEqualTo(modifiedCommentRequest.getCommentId()),
                () -> assertThat(comment.getContents()).isEqualTo(modifiedCommentRequest.getContents())
        );
    }

    @Test
    void 댓글_삭제() {
        // given
        given(commentRepository.findById(any())).willReturn(Optional.of(CommentFixture.댓글_조회())).willReturn(null);
        Post post = PostFixture.게시글();

        // when
        boolean isDeletedComment = commentService.deleteComment(post.getPostId());

        // then
        assertThat(isDeletedComment).isTrue();
    }
}