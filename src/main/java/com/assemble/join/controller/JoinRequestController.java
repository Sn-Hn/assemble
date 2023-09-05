package com.assemble.join.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.dto.response.JoinResponse;
import com.assemble.join.service.JoinRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "모임 가입 신청 Apis")
@RequiredArgsConstructor
@RequestMapping(path = "join")
@RestController
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @ApiOperation("모임 가입 신청")
    @PostMapping
    public ApiResult<JoinResponse> requestJoin(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        return ApiResult.ok(new JoinResponse(joinRequestService.requestJoinToAssemble(joinRequestDto)));
    }

    @ApiOperation("모임 가입 승인, 거절, 차단 ex) APPROVAL, REJECT, BLOCK")
    @PutMapping
    public ApiResult<JoinResponse> processJoinRequest(@RequestBody @Valid JoinRequestAnswer joinRequestAnswer) {
        return ApiResult.ok(new JoinResponse(joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer)));
    }

    @ApiOperation("모임 가입 취소")
    @PutMapping("cancel/{meetingId}")
    public ApiResult<Boolean> cancelJoin(@PathVariable Long meetingId) {
        return ApiResult.ok(joinRequestService.cancelJoinOfAssemble(meetingId));
    }

    @ApiOperation("모임 가입 신청 목록 조회")
    @GetMapping("{meetingId}")
    public ApiResult<List<JoinResponse>> getJoinRequestsToMeeting(@PathVariable Long meetingId) {
        return ApiResult.ok(joinRequestService.getJoinRequestsToMeeting(meetingId)
                        .stream()
                        .map(JoinResponse::new)
                        .collect(Collectors.toUnmodifiableList()));
    }

    @ApiOperation("내가 가입 신청한 모임 조회")
    @GetMapping
    public ApiResult<Page<JoinResponse>> getJoinRequests(Pageable pageable) {
        return ApiResult.ok(joinRequestService.getMyJoinRequests(pageable)
                .map(JoinResponse::new));
    }
}
