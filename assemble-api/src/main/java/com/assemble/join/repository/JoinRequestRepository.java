package com.assemble.join.repository;

import com.assemble.join.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long>, JoinRequestCustomRepository {
    @Query(value = "SELECT joinRequest.* " +
            "FROM join_request joinRequest " +
            "WHERE joinRequest.meeting_id = :meetingId " +
            "AND joinRequest.user_id = :userId " +
            "AND joinRequest.status != 'CANCEL' " +
            "ORDER BY joinRequest.modified_date desc " +
            "LIMIT 1", nativeQuery = true)
    Optional<JoinRequest> findByAssembleIdAndUserId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    @Query(value = "SELECT count(joinRequest) FROM JoinRequest joinRequest WHERE joinRequest.user.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
}
