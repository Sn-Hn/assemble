package com.assemble.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class ScheduleYearAndMonthRequest {

    @ApiModelProperty(value = "특정 연월 조회")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
    private YearMonth yearAndMonth;
}
