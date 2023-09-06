package com.assemble.join.dto.response;

import com.assemble.join.entity.JoinRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyJoinResponse {
    @ApiModelProperty(value = "가입 요청 ID")
    private Long joinRequestId;

    @ApiModelProperty(value = "모임 ID")
    private Long meetingId;

    @ApiModelProperty(value = "가입 신청 회원 ID")
    private Long userId;

    @ApiModelProperty(value = "가입 신청 회원 닉네임")
    private String nickname;

    @ApiModelProperty(value = "가입 신청 상태")
    private String status;

    @ApiModelProperty(value = "가입 요청 / 거절 메시지")
    private String message;

    @ApiModelProperty(value = "가입 요청 일시")
    private LocalDateTime createdDate;

    public MyJoinResponse(JoinRequest joinRequest) {
        this(
                joinRequest.getId(),
                joinRequest.getMeeting().getMeetingId(),
                joinRequest.getUser().getUserId(),
                joinRequest.getUser().getNickname(),
                joinRequest.getStatus().toString(),
                joinRequest.getRequestMessage(),
                joinRequest.getCreatedDate()
        );
    }
}
