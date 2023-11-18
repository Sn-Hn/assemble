package com.assemble.meeting.service;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.MeetingEvent;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingLikeRepository;
import com.assemble.meeting.repository.MeetingRepository;
import com.assemble.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingLikeRepository meetingLikeRepository;
    private final UserContext userContext;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Meeting createPost(MeetingCreationRequest meetingCreationRequest) {
        Meeting meeting = meetingCreationRequest.toEntity(userContext.getUserId());

        Meeting savedMeeting = meetingRepository.save(meeting);
        eventPublisher.publishEvent(new MeetingEvent(meeting));

        return savedMeeting;
    }

    @Transactional(readOnly = true)
    public Page<Meeting> getMeetings(MeetingSearchRequest meetingSearchRequest, Pageable pageable) {
        long count = meetingRepository.countBySearch(meetingSearchRequest);
        List<Meeting> meetings = meetingRepository.findAllBySearch(meetingSearchRequest, userContext.getUserId(), pageable);

        return new PageImpl<>(meetings, pageable, count);
    }

    @Transactional
    public Meeting getMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findByIdForUpdate(meetingId)
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingId));

        meeting.increaseHits();
        meeting.setIsLike(meetingLikeRepository.findPostByUser(meetingId, AuthenticationUtils.getUserId()).isPresent());

        return meeting;
    }

    @Transactional
    public Meeting modifyPost(ModifiedMeetingRequest modifiedMeetingRequest) {
        Meeting meeting = getMeetingOfWriter(modifiedMeetingRequest.getMeetingId());

        meeting.modifyPost(modifiedMeetingRequest);

        return meeting;
    }

    @Transactional
    public boolean deletePost(Long meetingId) {
        Meeting meeting = getMeetingOfWriter(meetingId);

        meetingRepository.delete(meeting);

        return true;
    }

    @Transactional(readOnly = true)
    public Page<Meeting> getMeetingsByUser(Long userId, Pageable pageable) {
        long count = meetingRepository.countByUserId(userId);
        List<Meeting> meetings = meetingRepository.findAllByUserId(userId, userContext.getUserId(), pageable);

        return new PageImpl<>(meetings, pageable, count);
    }

    private Meeting getMeetingOfWriter(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingId));

        if (!userContext.getUserId().equals(meeting.getUser().getUserId())) {
            throw new IllegalArgumentException("not writer");
        }

        return meeting;
    }
}
