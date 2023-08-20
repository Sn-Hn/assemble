package com.assemble.comment.dto.response;

import com.assemble.comment.entity.Comment;
import com.assemble.file.dto.response.ProfileResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCommentResponse {

    @ApiModelProperty(value = "모임 ID")
    private Long postId;

    @ApiModelProperty(value = "모임 제목")
    private String postTitle;

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

    public UserCommentResponse(Comment comment) {
        this(
                comment.getPost().getPostId(),
                comment.getPost().getTitle().getValue(),
                comment.getUser().getUserId(),
                comment.getCommentId(),
                comment.getContents(),
                comment.getUser().getNickname(),
                comment.getCreatedDate(),
                comment.getUser().toProfile()
        );
    }
}
