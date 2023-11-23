package com.assemble.meeting.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DelegationRequest {

    @ApiModelProperty(value = "모임 ID", required = true, example = "0")
    @NotNull
    private Long meetingId;

    @ApiModelProperty(value = "위임할 회원 ID", required = true, example = "0")
    @NotNull
    private Long userId;
}
