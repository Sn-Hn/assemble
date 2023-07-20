package com.assemble.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedCommentRequest {

    @ApiModelProperty(value = "댓글 ID", example = "0")
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용")
    private String contents;
}
