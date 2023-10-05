package com.assemble.comment.dto.response;

import com.assemble.comment.entity.Comment;
import com.assemble.file.dto.response.ProfileResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    @ApiModelProperty(value = "모임 ID")
    private Long meetingId;

    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "댓글 ID")
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용")
    private String contents;

    @ApiModelProperty(value = "작성자 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "작성일")
    private LocalDateTime writeDate;

    @ApiModelProperty(value = "작성자 프로필")
    private ProfileResponse profile;

    public CommentResponse(Comment comment) {
        this(
                comment.getMeeting().getMeetingId(),
                comment.getUser().getUserId(),
                comment.getCommentId(),
                comment.getContents(),
                comment.getUser().getNickname(),
                comment.getCreatedDate(),
                comment.getUser().toProfile()
        );
    }
}
