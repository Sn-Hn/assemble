package com.assemble.join.repository;

import com.assemble.join.entity.JoinRequest;

import java.util.List;

public interface JoinRequestCustomRepository {
    List<JoinRequest> findAllByPostId(Long meetingId);

    long countByPostId(Long meetingId);
}
