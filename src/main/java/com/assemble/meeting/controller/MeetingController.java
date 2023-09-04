package com.assemble.meeting.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.dto.response.MeetingCreationResponse;
import com.assemble.meeting.dto.response.MeetingResponse;
import com.assemble.meeting.dto.response.MeetingsResponse;
import com.assemble.meeting.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "모임 Apis")
@RequestMapping(path = "meeting")
@RequiredArgsConstructor
@RestController
public class MeetingController {

    private final MeetingService meetingService;

    @ApiOperation(value = "모임 등록")
    @PostMapping
    public ApiResult<MeetingCreationResponse> createPost(@RequestBody @Valid MeetingCreationRequest meetingCreationRequest) {
        return ApiResult.ok(new MeetingCreationResponse(meetingService.createPost(meetingCreationRequest)), HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "모임 목록 조회")
    @GetMapping
    public ApiResult<Page<MeetingsResponse>> getMeetings(MeetingSearchRequest meetingSearchRequest,
                                                      @PageableDefault(size = 12) Pageable pageable) {
        return ApiResult.ok(meetingService.getMeetings(meetingSearchRequest, pageable)
                .map(MeetingsResponse::new));
    }

    @ApiOperation(value = "모임 상세 조회")
    @GetMapping(path = "{meetingId}")
    public ApiResult<MeetingResponse> getMeeting(@PathVariable Long meetingId) {
        return ApiResult.ok(new MeetingResponse(meetingService.getMeeting(meetingId)));
    }

    @ApiOperation(value = "모임 수정")
    @PutMapping
    public ApiResult<MeetingResponse> modifyPost(@RequestBody @Valid ModifiedMeetingRequest modifiedMeetingRequest) {
        return ApiResult.ok(new MeetingResponse(meetingService.modifyPost(modifiedMeetingRequest)));
    }

    @ApiOperation(value = "모임 삭제")
    @DeleteMapping(path = "{meetingId}")
    public ApiResult<Boolean> deletePost(@PathVariable Long meetingId) {
        return ApiResult.ok(meetingService.deletePost(meetingId));
    }

    @ApiOperation(value = "특정 회원이 생성한 모임 조회")
    @GetMapping(path = "user/{userId}")
    public ApiResult<Page<MeetingsResponse>> getMeetingsByUser(@PathVariable Long userId,
                                                            @PageableDefault(size = 12) Pageable pageable) {
        return ApiResult.ok(meetingService.getMeetingsByUser(userId, pageable)
                .map(MeetingsResponse::new));
    }
}
