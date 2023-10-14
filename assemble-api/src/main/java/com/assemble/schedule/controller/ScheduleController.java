package com.assemble.schedule.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonth;
import com.assemble.schedule.dto.response.ScheduleCreationResponse;
import com.assemble.schedule.dto.response.ScheduleResponse;
import com.assemble.schedule.dto.response.ScheduleSimpleResponse;
import com.assemble.schedule.dto.response.SchedulesResponse;
import com.assemble.schedule.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "모임 일정 Apis")
@RequestMapping(path = "schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResult<ScheduleCreationResponse> registerSchedule(@RequestBody ScheduleCreationRequest scheduleCreationRequest) {
        return ApiResult.ok(ScheduleCreationResponse.toResponse(scheduleService.registerSchdule(scheduleCreationRequest)));
    }

    @GetMapping
    public ApiResult<List<SchedulesResponse>> getSchedulesByMonth(ScheduleYearAndMonth scheduleYearAndMonth) {
        Map<Integer, List<ScheduleSimpleResponse>> groupingSchedule = scheduleService.getSchedulesByYearAndMonth(scheduleYearAndMonth).stream()
                .map(ScheduleSimpleResponse::toResponse)
                .collect(Collectors.groupingBy(ScheduleSimpleResponse::getDay));

        return ApiResult.ok(groupingSchedule.keySet().stream()
                .map(key -> new SchedulesResponse(key, groupingSchedule.get(key)))
                .collect(Collectors.toUnmodifiableList()));
    }

    @GetMapping("{scheduleId}")
    public ApiResult<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ApiResult.ok(ScheduleResponse.toResponse(scheduleService.getSchedule(scheduleId)));
    }

    @PutMapping
    public ApiResult<ScheduleResponse> modifySchedule(ModifiedScheduleRequest modifiedScheduleRequest) {
        return ApiResult.ok(ScheduleResponse.toResponse(scheduleService.modifySchedule(modifiedScheduleRequest)));
    }

    @DeleteMapping("{scheduleId}")
    public ApiResult<Boolean> deleteSchedule(@PathVariable Long scheduleId) {
        return ApiResult.ok(scheduleService.deleteSchedule(scheduleId));
    }
}
