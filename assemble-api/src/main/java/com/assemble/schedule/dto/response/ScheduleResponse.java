package com.assemble.schedule.dto.response;

import com.assemble.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;

    private String title;

    private String content;

    private String writerNickname;

    private String date;

    private int day;

    private String writeDate;

    public static ScheduleResponse toResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getNickname(),
                schedule.getDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                schedule.getDate().getDayOfMonth(),
                schedule.getCreatedDate().format(DateTimeFormatter.BASIC_ISO_DATE)
        );
    }
}
