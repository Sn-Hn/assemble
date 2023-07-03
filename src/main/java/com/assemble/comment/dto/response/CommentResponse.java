package com.assemble.comment.dto.response;

import com.assemble.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;

    private String comment;

    private Long wrtierId;

    private LocalDateTime writedDate;

    public CommentResponse(Comment comment) {
        this (comment.getId(), comment.getComment(), comment.getCreator(), comment.getCreatedDate());
    }
}
