package com.assemble.meeting.service;

import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.PostEvent;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingRepository;
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
    private final MeetingLikeService meetingLikeService;
    private final UserContext userContext;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivityRepository activityRepository;

    @Transactional
    public Meeting createPost(MeetingCreationRequest meetingCreationRequest) {
        Meeting meeting = meetingCreationRequest.toEntity(userContext.getUserId());

        Meeting savedMeeting = meetingRepository.save(meeting);
        eventPublisher.publishEvent(new PostEvent(meeting));

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
        // TODO: 2023-07-22 리팩터링 필요 (조회수 계속 올라감) -신한
        meetingRepository.increaseHits(meetingId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingId));

        meeting.setIsLike(meetingLikeService.isAleadyLikeByUser(meetingId));

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
