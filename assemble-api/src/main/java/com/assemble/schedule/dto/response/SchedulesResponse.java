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
public class SchedulesResponse {

    @ApiModelProperty(value = "특정 연월 일정 목록")
    private List<ScheduleGroupingDay> schedules;
}
