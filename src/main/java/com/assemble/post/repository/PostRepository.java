package com.assemble.post.repository;

import com.assemble.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}
