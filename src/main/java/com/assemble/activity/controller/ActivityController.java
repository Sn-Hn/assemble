package com.assemble.activity.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.activity.dto.response.ActiveAssembleResponse;
import com.assemble.activity.dto.response.ActivityUserResponse;
import com.assemble.activity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "모임 가입 Apis")
@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @ApiOperation("활동 중인(가입한) 모임 조회")
    @GetMapping("activity/assemble")
    public ApiResult<Page<ActiveAssembleResponse>> getActiveAssembles(Pageable pageable) {
        return ApiResult.ok(activityService.getActiveAssembles(pageable)
                .map(ActiveAssembleResponse::new));
    }

    @ApiOperation("모임에서 활동 중인 회원 조회")
    @GetMapping("activity/user/{postId}")
    public ApiResult<Page<ActivityUserResponse>> getJoinUser(@PathVariable Long postId, Pageable pageable) {
        return ApiResult.ok(activityService.getJoinUserOfAssemble(postId, pageable)
                .map(ActivityUserResponse::new));
    }

    @ApiOperation("활동 중인 모임 탈퇴")
    @PutMapping("activity/withdrawal/{postId}")
    public ApiResult<Boolean> withdrawJoinAssemble(@PathVariable Long postId) {
        return ApiResult.ok(activityService.withdrawJoinAssemble(postId));
    }
}
