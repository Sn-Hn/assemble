package com.assemble.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifiedCommentRequest {

    @ApiModelProperty(value = "댓글 ID", example = "0")
    @NotNull
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용")
    @NotEmpty
    private String contents;
}
