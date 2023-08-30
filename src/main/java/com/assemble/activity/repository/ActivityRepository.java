package com.assemble.activity.repository;

import com.assemble.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityCustomRepository {

    @Query(value = "SELECT activity FROM Activity activity WHERE activity.post.postId = :postId AND activity.user.userId = :userId")
    Optional<Activity> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
