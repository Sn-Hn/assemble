package com.assemble.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifiedCommentRequest {

    @ApiModelProperty(value = "댓글 ID", example = "0")
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용")
    private String contents;
}
