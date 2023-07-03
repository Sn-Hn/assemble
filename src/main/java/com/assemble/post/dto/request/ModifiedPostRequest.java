package com.assemble.post.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedPostRequest {

    @ApiModelProperty(value = "게시글 번호", required = true)
    private Long postId;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String contents;

    @ApiModelProperty(value = "게시글 카테고리")
    private String category;

    @ApiModelProperty(value = "모집 인원")
    private int personnelNumber;

    @ApiModelProperty(value = "예상 기간")
    private int expectedPeriod;


}
