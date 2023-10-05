package com.assemble.activity.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.activity.entity.Activity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityUserResponse {

    @ApiModelProperty(value = "모임 ID")
    private Long meetingId;

    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "회원 닉네임")
    private String nickname;

    @ApiModelProperty(value = "회원 프로필 이미지")
    private ProfileResponse profile;

    @ApiModelProperty(value = "모임장 여부")
    private boolean isHost;

    public ActivityUserResponse(Activity activity) {
        this(
                activity.getMeeting().getMeetingId(),
                activity.getUser().getUserId(),
                activity.getUser().getNickname(),
                activity.getUser().toProfile(),
                activity.isHost()
        );
    }
}
