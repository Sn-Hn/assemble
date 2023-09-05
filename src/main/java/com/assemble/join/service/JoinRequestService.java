package com.assemble.join.service;

import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.entity.JoinRequest;
import com.assemble.join.repository.JoinRequestRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final MeetingRepository meetingRepository;
    private final UserContext userContext;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivityRepository activityRepository;

    @Transactional
    public JoinRequest requestJoinToAssemble(JoinRequestDto joinRequestDto) {
        joinRequestRepository.findByAssembleIdAndUserId(joinRequestDto.getMeetingId(), userContext.getUserId())
                .ifPresent(joinRequest -> {
                    joinRequest.validateAlreadyJoinRequest();
                    joinRequest.validateAnswerStatusOfJoinRequest();
                });

        activityRepository.findByMeetingId(joinRequestDto.getMeetingId())
                .stream()
                .filter(activity -> activity.isActivityUser(userContext.getUserId()))
                .findAny()
                .ifPresent(activity -> activity.validateAlreadyActivityUser(userContext.getUserId()));

        JoinRequest joinRequest = joinRequestDto.toEntity(userContext.getUserId());

        return joinRequestRepository.save(joinRequest);
    }

    @Transactional
    public JoinRequest processJoinRequestFromAssemble(JoinRequestAnswer joinRequestAnswer) {
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestAnswer.getJoinRequestId())
                .orElseThrow(() -> new NotFoundException(JoinRequest.class, joinRequestAnswer.getJoinRequestId()));

        joinRequest.validateBlock(joinRequestAnswer.getStatus());
        joinRequest.answerJoinRequest(joinRequestAnswer, userContext.getUserId());
        if (joinRequest.isApproval()) {
            eventPublisher.publishEvent(new JoinRequestEvent(joinRequest));
        }

        return joinRequest;
    }

    @Transactional
    public boolean cancelJoinOfAssemble(Long meetingId) {
        JoinRequest joinRequest = joinRequestRepository.findByAssembleIdAndUserId(meetingId, userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(JoinRequest.class, meetingId, userContext.getUserId()));

        joinRequest.cancelJoinRequest(userContext.getUserId());

        return true;
    }

    @Transactional(readOnly = true)
    public List<JoinRequest> getJoinRequestsToMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Meeting.class, meetingId));

        if (!meeting.getUser().getUserId().equals(userContext.getUserId())) {
            throw new IllegalArgumentException("모임장이 아닙니다.");
        }

        return joinRequestRepository.findAllByMeetingId(meetingId);
    }
}
