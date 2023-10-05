package com.assemble.meeting.dto.request;

import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingLikeRequest {

    @ApiModelProperty(value = "모임 ID", required = true, example = "1")
    @NotNull
    private Long meetingId;

    public Likes toEntity(Long userId) {
        return new Likes(new User(userId), new Meeting(this.meetingId));
    }
}
