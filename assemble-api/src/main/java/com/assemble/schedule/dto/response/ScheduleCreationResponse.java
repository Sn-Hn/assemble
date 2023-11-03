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
public class ScheduleCreationResponse {

    @ApiModelProperty(value = "일정 ID")
    private Long id;

    @ApiModelProperty(value = "일정 제목")
    private String title;

    @ApiModelProperty(value = "일정 내용")
    private String content;

    @ApiModelProperty(value = "날짜")
    private String date;

    @ApiModelProperty(value = "일정 작성일")
    private String writeDate;

    public static ScheduleCreationResponse toResponse(Schedule schedule) {
        return new ScheduleCreationResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                schedule.getCreatedDate().format(DateTimeFormatter.BASIC_ISO_DATE)
        );
    }
}
