package com.assemble.meeting.repository;

import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetingCustomRepository {

    List<Meeting> findAllBySearch(MeetingSearchRequest meetingSearchRequest, Long myUserId, Pageable pageable);

    List<Meeting> findAllByUserId(Long userId, Long myUserId, Pageable pageable);

    long countByUserId(Long userId);

    long countBySearch(MeetingSearchRequest meetingSearchRequest);

    Optional<Meeting> findByIdAndLikeForUpdate(Long meetingId, Long userId);
}
