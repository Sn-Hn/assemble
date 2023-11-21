package com.assemble.activity.controller;

import com.assemble.activity.dto.request.DismissUserRequest;
import com.assemble.activity.dto.response.DismissUserResponse;
import com.assemble.commons.response.ApiResult;
import com.assemble.activity.dto.response.ActiveAssembleResponse;
import com.assemble.activity.dto.response.ActivityUserResponse;
import com.assemble.activity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "활동 Apis")
@RequestMapping(path = "activity")
@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @ApiOperation("활동 중인(가입한) 모임 조회")
    @GetMapping("meeting")
    public ApiResult<Page<ActiveAssembleResponse>> getActiveAssembles(Pageable pageable) {
        return ApiResult.ok(activityService.getActiveAssembles(pageable)
                .map(ActiveAssembleResponse::new));
    }

    @ApiOperation("모임에서 활동 중인 회원 조회")
    @GetMapping("user/{meetingId}")
    public ApiResult<List<ActivityUserResponse>> getJoinUser(@PathVariable Long meetingId) {
        return ApiResult.ok(activityService.getJoinUserOfAssemble(meetingId)
                        .stream()
                        .map(ActivityUserResponse::new)
                        .collect(Collectors.toUnmodifiableList()));
    }

    @ApiOperation("활동 중인 모임 탈퇴")
    @PutMapping("withdrawal/{meetingId}")
    public ApiResult<Boolean> withdrawJoinAssemble(@PathVariable Long meetingId) {
        return ApiResult.ok(activityService.withdrawJoinAssemble(meetingId));
    }

    @ApiOperation("활동 중인 회원 강제 퇴장")
    @PutMapping("dismissal")
    public ApiResult<Boolean> dismissUser(@RequestBody DismissUserRequest dismissUserRequest) {
        return ApiResult.ok(activityService.dismissUserOfMeeting(dismissUserRequest));
    }
}
