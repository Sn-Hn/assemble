package com.assemble.comment.service;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.comment.repository.CommentRepository;
import com.assemble.commons.exception.NotFoundException;
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
        Comment comment = commentRepository.findById(modifiedCommentRequest.getCommentId())
                .orElseThrow(() -> new NotFoundException(Comment.class, modifiedCommentRequest.getCommentId()));

        comment.modifyComment(modifiedCommentRequest);
        return comment;
    }

    public boolean deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        commentRepository.delete(comment);

        return true;
    }
}
