package com.assemble.post.repository;

import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Likes;

import java.util.Optional;

public interface PostLikeCustomRepository {
    Optional<Likes> findPostByUser(Long postId, Long myUserId);
}
