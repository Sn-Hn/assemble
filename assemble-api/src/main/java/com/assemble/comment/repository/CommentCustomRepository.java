package com.assemble.comment.repository;

import com.assemble.comment.entity.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findByUser(Long userId, Pageable pageable);

    long countByUserId(Long userId);
}
