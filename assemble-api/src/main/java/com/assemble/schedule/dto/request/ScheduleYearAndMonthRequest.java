package com.assemble.schedule.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleYearAndMonthRequest {

    @ApiModelProperty(value = "특정 연월 조회")
    private String yearAndMonth;
}
