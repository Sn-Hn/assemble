package com.assemble.meeting.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifiedMeetingRequest {

    @ApiModelProperty(value = "모임 번호", required = true, example = "0")
    @NotNull
    private Long meetingId;

    @ApiModelProperty(value = "모임 이름")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "모임 설명")
    @NotEmpty
    private String description;

    @ApiModelProperty(value = "모임 카테고리", example = "0")
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "모집 인원", example = "0")
    @NotNull
    private int personnelNumber;

    @ApiModelProperty(value = "예상 기간", example = "0")
    @NotNull
    private int expectedPeriod;

    @ApiModelProperty(value = "모임 상태 (모집 중, 모집 완료)")
    @NotEmpty
    private String meetingStatus;

}
