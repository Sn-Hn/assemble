package com.assemble.commons.converter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@ApiModel
public class PageableConverter {

    @ApiModelProperty(value = "페이지 번호 (0, ..., N)", example = "0")
    private Integer page;

    @ApiModelProperty(value = "페이지 사이즈 (default 12)", example = "12")
    private Integer size;

    @ApiModelProperty(value = "정렬 (컬럼명,acs|desc)")
    private String sort;
}
