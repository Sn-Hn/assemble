package com.assemble.meeting.dto.response;

import com.assemble.meeting.entity.Meeting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DelegationResponse {

    @ApiModelProperty(value = "모임 ID")
    private Long meetingId;

    @ApiModelProperty(value = "위임한 모임장 ID")
    private Long userId;

    public static DelegationResponse toResponse(Meeting meeting) {
        return new DelegationResponse(meeting.getMeetingId(), meeting.getUser().getUserId());
    }
}
