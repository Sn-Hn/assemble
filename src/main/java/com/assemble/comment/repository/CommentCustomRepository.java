package com.assemble.comment.repository;

import com.assemble.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findByUser(Long userId, Pageable pageable, long count);

    long countByUserId(Long userId);
}
