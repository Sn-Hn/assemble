package com.assemble.post.dto.response;

import com.assemble.comment.dto.response.CommentResponse;
import com.assemble.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long postId;

    private String title;

    private String contents;

    private Long writerId;

    private String writerNickname;

    private Long hits;

    private int perssonelNumber;

    private int expectedPeriod;

    private int commentCount;

    private String categoryName;

    private List<CommentResponse> comments;

    public PostResponse(Post post) {
        this(
                post.getPostId(),
                post.getTitle().getValue(),
                post.getContents().getValue(),
                post.getUser().getUserId(),
                post.getUser().getNickName(),
                post.getHits(),
                post.getPersonnelNumber(),
                post.getExpectedPeriod(),
                post.getComments().getComments().size(),
                post.getCategory().getName().getValue(),
                post.getComments().getComments().stream().map(CommentResponse::new).collect(Collectors.toList())
        );
    }
}
