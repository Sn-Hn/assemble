package com.assemble.comment.dto.request;

import com.assemble.comment.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CommentCreationRequest {

    @ApiModelProperty(value = "게시글(모임) ID", example = "0")
    @NotNull
    private Long postId;

    @ApiModelProperty(value = "댓글 내용")
    @NotEmpty
    private String contents;

    private CommentCreationRequest() {
    }

    public Comment toEntity(Long writerId) {
        return new Comment(postId, writerId, contents);
    }
}
