package com.assemble.activity.repository;

import com.assemble.activity.entity.Activity;
import com.assemble.meeting.entity.Meeting;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityCustomRepository {
    long countByActiveAssembles(Long userId);

    List<Meeting> findAllByActiveAssembles(Long userId, Pageable pageable);

    long countByUserOfAssemble(Long meetingId);

    List<Activity> findAllByUserOfAssemble(Long meetingId);
}
