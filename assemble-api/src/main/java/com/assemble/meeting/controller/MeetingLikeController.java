package com.assemble.meeting.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.meeting.dto.request.MeetingLikeRequest;
import com.assemble.meeting.dto.response.MeetingsResponse;
import com.assemble.meeting.service.MeetingLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "모임 좋아요 Apis")
@RequiredArgsConstructor
@RestController
public class MeetingLikeController {

    private final MeetingLikeService meetingLikeService;

    @ApiOperation(value = "모임 좋아요")
    @PostMapping("meeting/like")
    public ApiResult<Boolean> like(@RequestBody @Valid MeetingLikeRequest meetingLikeRequest) {
        return ApiResult.ok(meetingLikeService.likePost(meetingLikeRequest), HttpStatus.CREATED);
    }

    @ApiOperation(value = "모임 좋아요 취소")
    @DeleteMapping("meeting/like/{meetingId}")
    public ApiResult<Boolean> cancelLike(@PathVariable Long meetingId) {
        return ApiResult.ok(meetingLikeService.cancelLikePost(meetingId));
    }


    @ApiOperation(value = "내가 좋아요 한 모임")
    @GetMapping(path = "meeting/like")
    public ApiResult<Page<MeetingsResponse>> getMeetingsByLike(Pageable pageable) {
        return ApiResult.ok(meetingLikeService.getMeetingsByLike(pageable)
                .map(MeetingsResponse::new));
    }
}
