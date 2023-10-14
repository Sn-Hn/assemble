package com.assemble.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedScheduleRequest {

    private Long id;

    private String title;

    private String content;
}
