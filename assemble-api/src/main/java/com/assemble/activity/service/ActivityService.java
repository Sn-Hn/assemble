package com.assemble.activity.service;

import com.assemble.activity.dto.request.DismissUserRequest;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingRepository;
import com.assemble.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MeetingRepository meetingRepository;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public Page<Meeting> getActiveAssembles(Pageable pageable) {
        long count = activityRepository.countByActiveAssembles(userContext.getUserId());
        List<Meeting> activityAssembles = activityRepository.findAllByActiveAssembles(userContext.getUserId(), pageable);

        return new PageImpl<>(activityAssembles, pageable, count);
    }

    @Transactional(readOnly = true)
    public List<Activity> getJoinUserOfAssemble(Long meetingId) {
        return activityRepository.findAllByUserOfAssemble(meetingId);
    }

    @Transactional
    public boolean withdrawJoinAssemble(Long meetingId) {
        Activity activity = activityRepository.findByMeetingIdAndUserId(meetingId, userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(Activity.class, meetingId, userContext.getUserId()));

        activity.withdraw(userContext.getUserId());

        return true;
    }

    @Transactional
    public boolean dismissUserOfMeeting(DismissUserRequest dismissUserRequest) {
        Meeting meeting = meetingRepository.findById(dismissUserRequest.getMeetingId())
                .orElseThrow(() -> new NotFoundException(Meeting.class, dismissUserRequest.getMeetingId()));

        meeting.isHost(AuthenticationUtils.getUserId());

        Activity activity = activityRepository.findByMeetingIdAndUserId(dismissUserRequest.getMeetingId(), dismissUserRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("강퇴할 회원이 모임에 존재하지 않습니다."));

        activity.dismiss();

        return true;
    }
}
