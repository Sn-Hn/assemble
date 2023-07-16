package com.assemble.comment.service;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(CommentCreationRequest commentCreationRequest) {
        Comment comment = commentCreationRequest.toEntity();

        return commentRepository.save(comment);
    }

    public Comment modifyComment(ModifiedCommentRequest modifiedCommentRequest) {
        return modifiedCommentRequest.toEntity();
    }

    public boolean deleteComment(Long commentId) {
        Comment comment = new Comment(commentId);
        commentRepository.delete(comment);

        return true;
    }
}
