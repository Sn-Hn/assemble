package com.assemble.post.repository;

import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllBySearch(PostSearchRequest postSearchRequest, Long myUserId, Pageable pageable);

    List<Post> findAllByUserId(Long userId, Long myUserId, Pageable pageable);

    long countByUserId(Long userId);

    long countBySearch(PostSearchRequest postSearchRequest);
}
