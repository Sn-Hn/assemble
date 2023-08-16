package com.assemble.post.fixture;

import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;

public class PostLikeFixture {
    private static final Long postId = 1L;
    private static final Long userId = 2L;
    private static final Long aleadyLikeUserId = 1L;

    public static PostLikeRequest 게시글_좋아요_요청() {
        return new PostLikeRequest(postId);
    }

    public static PostLikeRequest 게시글_좋아요_취소_요청() {
        return new PostLikeRequest(postId);
    }

    public static Likes 좋아요_객체() {
        return new Likes(new User(userId), new Post(postId));
    }
}
