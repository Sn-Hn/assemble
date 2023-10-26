package com.assemble.schedule.dto.response;

import com.assemble.schedule.entity.Schedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSimpleResponse {

    @ApiModelProperty(value = "일정 ID")
    private Long id;

    @ApiModelProperty(value = "일정 제목")
    private String title;

    @ApiModelProperty(value = "일정 작성자 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "일정 시작 날짜")
    private String startDate;

    @ApiModelProperty(value = "일정 종료 날짜")
    private String endDate;

    @ApiModelProperty(value = "일정 작성일")
    private String writeDate;

    public static ScheduleSimpleResponse toResponse(Schedule schedule) {
        return new ScheduleSimpleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getUser().getNickname(),
                schedule.getStartDate().format(DateTimeFormatter.ISO_DATE),
                schedule.getEndDate().format(DateTimeFormatter.ISO_DATE),
                schedule.getCreatedDate().format(DateTimeFormatter.ISO_DATE)
        );
    }
}
