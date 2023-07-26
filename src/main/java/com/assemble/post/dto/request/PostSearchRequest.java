package com.assemble.post.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchRequest {

    @ApiModelProperty(value = "검색어 ex) 제목")
    private String searchQuery;

    @ApiModelProperty(value = "검색할 주제 ex) title,writer,contents")
    private String searchBy;

    @ApiModelProperty(value = "카테고리 ID", example = "1")
    private Long categoryId;
}
