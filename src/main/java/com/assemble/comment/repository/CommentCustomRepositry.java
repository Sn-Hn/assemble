package com.assemble.comment.repository;

import com.assemble.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepositry {
    Page<Comment> findByUser(Long userId, Pageable pageable);
}
