package com.assemble.comment.dto.request;

import com.assemble.comment.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreationRequest {

    @ApiModelProperty(value = "게시글(모임) ID", example = "0")
    private Long postId;

    @ApiModelProperty(value = "회원 ID", example = "0")
    private Long userId;

    @ApiModelProperty(value = "댓글 내용")
    private String contents;

    private CommentCreationRequest() {
    }

    public Comment toEntity() {
        return new Comment(postId, userId, contents);
    }
}
