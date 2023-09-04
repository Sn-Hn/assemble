package com.assemble.activity.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.meeting.entity.Meeting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActiveAssembleResponse {

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

    @ApiModelProperty(value = "모임 모집인원")
    private int personnelNumber;

    @ApiModelProperty(value = "모임 예상 기간")
    private int expectedPeriod;

    @ApiModelProperty(value = "모임 댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "카테고리 이름")
    private String categoryName;

    @ApiModelProperty(value = "작성일")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "좋아요 여부")
    private boolean isLikeStatus;

    @ApiModelProperty(value = "모임 상태 (모집 중, 모집 완료)")
    private String meetingStatus;

    public ActiveAssembleResponse(Meeting meeting) {
        this(
                meeting.getMeetingId(),
                meeting.getName().getValue(),
                meeting.getDescription().getValue(),
                meeting.getUser().getUserId(),
                meeting.getUser().getNickname(),
                meeting.getUser().toProfile(),
                meeting.getHits(),
                meeting.getLikes(),
                meeting.getPersonnelNumber(),
                meeting.getExpectedPeriod(),
                meeting.getComments().getComments().size(),
                meeting.getCategory().getName(),
                meeting.getCreatedDate(),
                meeting.isLike(),
                meeting.getMeetingStatus().toString()
        );
    }
}
