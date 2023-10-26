package com.assemble.schedule.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.dto.response.*;
import com.assemble.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ApiResult.ok(ScheduleCreationResponse.toResponse(scheduleService.registerSchdule(scheduleCreationRequest)), HttpStatus.CREATED);
    }

    @GetMapping
    public ApiResult<List<ScheduleSimpleResponse>> getSchedulesByMonth(ScheduleYearAndMonthRequest scheduleYearAndMonthRequest) {
        return ApiResult.ok(scheduleService.findSchedulesByYearAndMonth(scheduleYearAndMonthRequest).stream()
                .map(ScheduleSimpleResponse::toResponse)
                .collect(Collectors.toUnmodifiableList()));
    }

    @GetMapping("{scheduleId}")
    public ApiResult<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ApiResult.ok(ScheduleResponse.toResponse(scheduleService.findScheduleById(scheduleId)));
    }

    @PutMapping
    public ApiResult<ScheduleResponse> modifySchedule(@RequestBody ModifiedScheduleRequest modifiedScheduleRequest) {
        return ApiResult.ok(ScheduleResponse.toResponse(scheduleService.modifySchedule(modifiedScheduleRequest)));
    }

    @DeleteMapping("{scheduleId}")
    public ApiResult<Boolean> deleteSchedule(@PathVariable Long scheduleId) {
        return ApiResult.ok(scheduleService.deleteSchedule(scheduleId));
    }
}
