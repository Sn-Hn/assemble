package com.assemble.meeting.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.meeting.dto.request.MeetingLikeRequest;
import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingLikeRepository;
import com.assemble.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingLikeService {

    private final MeetingLikeRepository meetingLikeRepository;
    private final MeetingRepository meetingRepository;
    private final UserContext userContext;

    @Transactional
    public boolean likePost(MeetingLikeRequest meetingLikeRequest) {
        Meeting meeting = meetingRepository.findById(meetingLikeRequest.getMeetingId())
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingLikeRequest.getMeetingId()));

        Likes meetingLike = meetingLikeRequest.toEntity(userContext.getUserId());
        if (isAleadyLikeByUser(meetingLikeRequest.getMeetingId())) {
            throw new IllegalArgumentException("this meeting is already like");
        }

        meetingLikeRepository.save(meetingLike);

        meetingRepository.increaseLikes(meeting.getMeetingId());

        return true;
    }

    @Transactional
    public boolean cancelLikePost(Long meetingId) {
        Likes likes = meetingLikeRepository.findPostByUser(meetingId, userContext.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("this meeting didn't like it"));

        meetingLikeRepository.delete(likes);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingId));

        meeting.validateLikeCount();

        meetingRepository.decreaseLikes(meeting.getMeetingId());

        return true;
    }

    @Transactional(readOnly = true)
    public boolean isAleadyLikeByUser(Long meetingId) {
        if (meetingLikeRepository.findPostByUser(meetingId, userContext.getUserId()).isPresent()) {
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public Page<Meeting> getMeetingsByLike(Pageable pageable) {
        Long userId = userContext.getUserId();
        long count = meetingLikeRepository.countByUserId(userId);
        List<Meeting> meetings = meetingLikeRepository.findAllByUserId(userId, pageable).stream()
                .map(like -> {
                    Meeting meeting = like.getMeeting();
                    meeting.setIsLike(true);
                    return meeting;
                })
                .collect(Collectors.toUnmodifiableList());

        return new PageImpl<>(meetings, pageable, count);
    }
}
