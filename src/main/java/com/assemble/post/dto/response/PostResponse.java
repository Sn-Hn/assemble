package com.assemble.post.dto.response;

import com.assemble.comment.dto.response.CommentResponse;
import com.assemble.post.entity.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PostResponse {

    @ApiModelProperty(value = "게시글 ID")
    private Long postId;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String contents;

    @ApiModelProperty(value = "회원 ID")
    private Long writerId;

    @ApiModelProperty(value = "회원 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "게시글 조회수")
    private Long hits;

    @ApiModelProperty(value = "게시글 좋아요 수")
    private Long likes;

    @ApiModelProperty(value = "게시글 모집인원")
    private int perssonelNumber;

    @ApiModelProperty(value = "게시글 예상 기간")
    private int expectedPeriod;

    @ApiModelProperty(value = "게시글 댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "카테고리 이름")
    private String categoryName;

    @ApiModelProperty(value = "댓글")
    private List<CommentResponse> comments;

    public PostResponse(Post post) {
        this(
                post.getPostId(),
                post.getTitle().getValue(),
                post.getContents().getValue(),
                post.getUser().getUserId(),
                post.getUser().getNickName(),
                post.getHits(),
                post.getLikes(),
                post.getPersonnelNumber(),
                post.getExpectedPeriod(),
                post.getComments().getComments().size(),
                post.getCategory().getName().getValue(),
                post.getComments().getComments().stream()
                        .map(CommentResponse::new)
                        .collect(Collectors.toList())
        );
    }
}
