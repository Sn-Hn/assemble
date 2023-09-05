package com.assemble.join.repository;

import com.assemble.join.entity.JoinRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JoinRequestCustomRepository {
    List<JoinRequest> findAllByMeetingId(Long meetingId);

    long countByMeetingId(Long meetingId);

    List<JoinRequest> findAllByUserId(Long userId, Pageable pageable);
}
