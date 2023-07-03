package com.assemble.post.repository;

import com.assemble.post.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<Likes, Long>, PostLikeCustomRepository {
}
