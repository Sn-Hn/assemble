package com.assemble.meeting.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingSearchRequest {

    @ApiModelProperty(value = "검색어 ex) 이름")
    private String searchQuery;

    @ApiModelProperty(value = "검색할 주제 ex) name,writer,contents")
    private String searchBy;

    @ApiModelProperty(value = "카테고리 ID", example = "1")
    private Long categoryId;
}
