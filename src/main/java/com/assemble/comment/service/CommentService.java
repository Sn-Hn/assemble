package com.assemble.comment.service;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.comment.repository.CommentRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserContext userContext;

    @Transactional
    public Comment createComment(CommentCreationRequest commentCreationRequest) {
        Comment comment = commentCreationRequest.toEntity(userContext.getUserId());

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment modifyComment(ModifiedCommentRequest modifiedCommentRequest) {
        Comment comment = getComment(modifiedCommentRequest.getCommentId());

        comment.modifyComment(modifiedCommentRequest);
        return comment;
    }

    @Transactional
    public boolean deleteComment(Long commentId) {
        Comment comment = getComment(commentId);

        commentRepository.delete(comment);

        return true;
    }

    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByUser(Long userId, Pageable pageable) {
        long commentCountByUser = commentRepository.countByUserId(userId);
        return new PageImpl<>(commentRepository.findByUser(userId, pageable, commentCountByUser));
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        if (!userContext.getUserId().equals(comment.getUser().getUserId())) {
            throw new IllegalStateException("not writer");
        }
        return comment;
    }
}
