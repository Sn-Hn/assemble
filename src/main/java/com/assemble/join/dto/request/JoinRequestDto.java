package com.assemble.join.dto.request;

import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.entity.JoinRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JoinRequestDto {

    @ApiModelProperty(value = "모임 ID", example = "1")
    @NotNull
    private Long meetingId;

    @ApiModelProperty(value = "가입 신청 메시지", example = "가입 신청합니다~")
    @NotNull
    private String joinRequestMessage;

    public JoinRequest toEntity(Long userId) {
        return new JoinRequest(
                new Meeting(meetingId),
                new User(userId),
                JoinRequestStatus.REQUEST,
                joinRequestMessage);
    }
}
