package com.assemble.join.repository;

import com.assemble.join.entity.JoinRequest;

import java.util.List;

public interface JoinRequestCustomRepository {
    List<JoinRequest> findAllByMeetingId(Long meetingId);

    long countByMeetingId(Long meetingId);
}
