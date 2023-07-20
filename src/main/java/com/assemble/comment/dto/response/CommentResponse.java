package com.assemble.comment.dto.response;

import com.assemble.comment.entity.Comment;
import com.assemble.file.dto.response.ProfileResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponse {

    @ApiModelProperty(value = "게시글 ID")
    private Long postId;

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
    private List<ProfileResponse> profile;

    public CommentResponse(Comment comment) {
        this(
                comment.getPost().getPostId(),
                comment.getUser().getUserId(),
                comment.getCommentId(),
                comment.getContents(),
                comment.getUser().getNickName(),
                comment.getCreatedDate(),
                comment.getUser().toUserProfileResponse()
        );
    }
}
