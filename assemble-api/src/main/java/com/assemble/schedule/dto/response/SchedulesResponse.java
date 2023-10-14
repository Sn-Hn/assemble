package com.assemble.schedule.dto.response;

import com.assemble.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SchedulesResponse {

    private int day;

    private List<ScheduleSimpleResponse> schdulesOfMonth;
}
