package com.assemble.post.dto.request;

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
public class ModifiedPostRequest {

    @ApiModelProperty(value = "게시글 번호", required = true, example = "0")
    @NotNull
    private Long postId;

    @ApiModelProperty(value = "게시글 제목")
    @NotEmpty
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    @NotEmpty
    private String contents;

    @ApiModelProperty(value = "게시글 카테고리", example = "0")
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "모집 인원", example = "0")
    @NotNull
    private int personnelNumber;

    @ApiModelProperty(value = "예상 기간", example = "0")
    @NotNull
    private int expectedPeriod;

    @ApiModelProperty(value = "모임 상태 (모집 중, 모집 완료)")
    @NotEmpty
    private String postStatus;

}
