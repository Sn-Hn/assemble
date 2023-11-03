package com.assemble.schedule.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleGroupingDay {

    @ApiModelProperty(value = "특정 연월의 일")
    private int day;

    @ApiModelProperty(value = "해당 연월일의 일정 목록")
    private List<ScheduleSimpleResponse> schedulesOfMonth;
}
