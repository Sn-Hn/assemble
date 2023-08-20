package com.assemble.post.repository;

import com.assemble.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    @Modifying
    @Query(value = "UPDATE Post post SET post.hits = post.hits + 1 WHERE post.postId = :postId")
    void increaseHits(@Param(value = "postId") Long postId);

    @Modifying
    @Query(value = "UPDATE Post post SET post.likes = post.likes + 1 WHERE post.postId = :postId")
    void increaseLikes(@Param(value = "postId")Long postId);

    @Modifying
    @Query(value = "UPDATE Post post SET post.likes = post.likes - 1 WHERE post.postId = :postId")
    void decreaseLikes(@Param(value = "postId")Long postId);
}
