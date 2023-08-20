package com.assemble.post.repository;

import com.assemble.post.entity.Likes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostLikeCustomRepository {
    Optional<Likes> findPostByUser(Long postId, Long myUserId);

    List<Likes> findAllByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);
}
