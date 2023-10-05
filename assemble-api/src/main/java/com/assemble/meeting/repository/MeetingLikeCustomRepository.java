package com.assemble.meeting.repository;

import com.assemble.meeting.entity.Likes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetingLikeCustomRepository {
    Optional<Likes> findPostByUser(Long meetingId, Long myUserId);

    List<Likes> findAllByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);
}
