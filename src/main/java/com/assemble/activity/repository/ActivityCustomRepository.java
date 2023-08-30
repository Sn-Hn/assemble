package com.assemble.activity.repository;

import com.assemble.activity.entity.Activity;
import com.assemble.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityCustomRepository {
    long countByActiveAssembles(Long userId);

    List<Post> findAllByActiveAssembles(Long userId, Pageable pageable);

    long countByUserOfAssemble(Long postId);

    List<Activity> findAllByUserOfAssemble(Long postId, Pageable pageable);
}
