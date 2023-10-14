package com.assemble.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduleYearAndMonth {

    private String yearAndMonth;
}
