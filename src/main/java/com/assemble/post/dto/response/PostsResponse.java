package com.assemble.post.dto.response;

import com.assemble.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostsResponse {

    private Long postId;

    private String title;

    private String contents;

    private String writerNickname;

    private Long hits;

    private Long likes;

    private int perssonelNumber;

    private int expectedPeriod;

    private int commentCount;

    private String categoryName;

    public PostsResponse(Post post) {
        this(
                post.getPostId(),
                post.getTitle().getValue(),
                post.getContents().getValue(),
                post.getUser().getNickName(),
                post.getHits(),
                post.getLikes(),
                post.getPersonnelNumber(),
                post.getExpectedPeriod(),
                post.getComments().getComments().size(),
                post.getCategory().getName().getValue()
        );
    }
}
