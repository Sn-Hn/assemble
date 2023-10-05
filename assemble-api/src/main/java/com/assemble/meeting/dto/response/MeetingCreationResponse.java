package com.assemble.meeting.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.meeting.entity.Meeting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ApiModel(value = "PostCreationResponse : 모임 작성 응답 값")
@AllArgsConstructor
@Getter
@ToString
public class MeetingCreationResponse {

    @ApiModelProperty(value = "모임 이름")
    private String name;

    @ApiModelProperty(value = "모임 설명")
    private String description;

    @ApiModelProperty(value = "모임 카테고리")
    private Long categoryId;

    @ApiModelProperty(value = "작성자 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "작성자 ID")
    private Long writerId;

    @ApiModelProperty(value = "모임 조회수")
    private Long hits;

    @ApiModelProperty(value = "모임 좋아요 수")
    private Long likeCount;

    @ApiModelProperty(value = "모임 프로필 사진 목록")
    private List<ProfileResponse> meetingProfile;

    @ApiModelProperty(value = "모임 상태 (모집 중, 모집 완료)")
    private String meetingStatus;

    @ApiModelProperty(value = "모임에 활동 중인 인원")
    private int activityUserCount;

    @ApiModelProperty(value = "주소")
    private String address;

    public MeetingCreationResponse(Meeting meeting) {
        this(
                meeting.getName().getValue(),
                meeting.getDescription().getValue(),
                meeting.getCategory().getId(),
                meeting.getUser().getNickname(),
                meeting.getUser().getUserId(),
                meeting.getHits(),
                0L,
                meeting.toMeetingProfileResponse(),
                meeting.getMeetingStatus().toString(),
                meeting.getActivities().getValues().size(),
                meeting.getAddress().getValue()
        );
    }
}
