package com.assemble.join.repository;

import com.assemble.join.entity.JoinRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JoinRequestCustomRepository {
    List<JoinRequest> findAllByPostId(Long postId, Pageable pageable);

    long countByPostId(Long postId);
}
