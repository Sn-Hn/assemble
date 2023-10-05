package com.assemble.meeting.dto.response;

import com.assemble.comment.dto.response.CommentResponse;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.meeting.entity.Meeting;
import com.assemble.util.AuthenticationUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MeetingResponse {

    @ApiModelProperty(value = "모임 ID")
    private Long meetingId;

    @ApiModelProperty(value = "모임 이름")
    private String name;

    @ApiModelProperty(value = "모임 설명")
    private String description;

    @ApiModelProperty(value = "작성자 ID")
    private Long writerId;

    @ApiModelProperty(value = "작성자 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "작성자 프로필 이미지")
    private ProfileResponse writerProfileImages;

    @ApiModelProperty(value = "모임 조회수")
    private Long hits;

    @ApiModelProperty(value = "모임 좋아요 수")
    private Long likes;

    @ApiModelProperty(value = "모임 댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "카테고리 이름")
    private String categoryName;

    @ApiModelProperty(value = "댓글")
    private List<CommentResponse> comments;

    @ApiModelProperty(value = "모임 프로필 이미지")
    private List<ProfileResponse> meetingProfileImages;

    @ApiModelProperty(value = "작성일")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "좋아요 여부")
    private boolean isLikeStatus;

    @ApiModelProperty(value = "모임 상태 (모집 중, 모집 완료)")
    private String meetingStatus;

    @ApiModelProperty(value = "모임에서 활동 중인 인원")
    private int activityUserCount;

    @ApiModelProperty(value = "주소")
    private String address;

    @ApiModelProperty(value = "활동 여부")
    private boolean isActivity;

    @ApiModelProperty(value = "가입 신청 여부")
    private boolean isJoinRequest;

    public MeetingResponse(Meeting meeting) {
        this(
                meeting.getMeetingId(),
                meeting.getName().getValue(),
                meeting.getDescription().getValue(),
                meeting.getUser().getUserId(),
                meeting.getUser().getNickname(),
                meeting.getUser().toProfile(),
                meeting.getHits(),
                meeting.getLikes(),
                meeting.getComments().getComments().size(),
                meeting.getCategory().getName(),
                meeting.getComments().getComments().stream()
                        .map(CommentResponse::new)
                        .collect(Collectors.toList()),
                meeting.toMeetingProfileResponse(),
                meeting.getCreatedDate(),
                meeting.isLike(),
                meeting.getMeetingStatus().toString(),
                meeting.getActivities().getValues().size(),
                meeting.getAddress().getValue(),
                meeting.getActivities().isActivityUser(AuthenticationUtils.getUserId()),
                meeting.getJoinRequests().isJoinRequestUser(AuthenticationUtils.getUserId())
        );
    }
}
