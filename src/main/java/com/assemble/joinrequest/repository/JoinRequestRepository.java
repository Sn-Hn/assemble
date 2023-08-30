package com.assemble.joinrequest.repository;

import com.assemble.joinrequest.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long>, JoinRequestCustomRepository {
    @Query(value = "SELECT joinRequest.* " +
            "FROM join_request joinRequest " +
            "WHERE joinRequest.post_id = :postId " +
            "AND joinRequest.user_id = :userId " +
            "AND joinRequest.status != 'CANCEL' " +
            "ORDER BY joinRequest.modified_date desc " +
            "LIMIT 1", nativeQuery = true)
    Optional<JoinRequest> findByAssembleIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
