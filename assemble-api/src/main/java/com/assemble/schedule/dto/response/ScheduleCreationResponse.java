package com.assemble.schedule.dto.response;

import com.assemble.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ScheduleCreationResponse {
    private Long id;

    private String title;

    private String content;

    private String date;

    private int day;

    private String writeDate;

    public static ScheduleCreationResponse toResponse(Schedule schedule) {
        return new ScheduleCreationResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                schedule.getCreatedDate().getDayOfMonth(),
                schedule.getCreatedDate().format(DateTimeFormatter.BASIC_ISO_DATE)
        );
    }
}
