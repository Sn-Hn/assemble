package com.assemble.post.repository;

import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {

    Page<Post> findAllBySearch(PostSearchRequest postSearchRequest, Long myUserId, Pageable pageable, long count);

    Page<Post> findAllByUserId(Long userId, Long myUserId, Pageable pageable, long count);

    long countByUserId(Long userId);

    long countBySearch(PostSearchRequest postSearchRequest);
}
