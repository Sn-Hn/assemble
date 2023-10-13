package com.assemble.join.service;

import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.entity.JoinRequest;
import com.assemble.join.repository.JoinRequestRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingRepository;
import com.assemble.noti.event.NotificationEvent;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import com.assemble.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinRequestService {

    private final String JOIN_REQUEST_MESSAGE = "web.notification.message.join.request";
    private final String JOIN_APPROVAL_MESSAGE = "web.notification.message.join.approval";
    private final String JOIN_REJECT_MESSAGE = "web.notification.message.join.reject";

    private final JoinRequestRepository joinRequestRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;
    private final ActivityRepository activityRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationEvent notificationEvent;

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

        JoinRequest savedJoinRequest = joinRequestRepository.save(joinRequest);

        User user = userRepository.findById(userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(User.class, userContext.getUserId()));
        Meeting meeting = meetingRepository.findById(joinRequestDto.getMeetingId())
                .orElseThrow(() -> new NotFoundException(Meeting.class, joinRequestDto.getMeetingId()));

        notificationEvent.publish(
                savedJoinRequest.getUser().getUserId(),
                JOIN_REQUEST_MESSAGE,
                meeting.getUser().getFcmToken(),
                user.getNickname(), meeting.getName().getValue());

        return savedJoinRequest;
    }

    @Transactional
    public JoinRequest processJoinRequestFromAssemble(JoinRequestAnswer joinRequestAnswer) {
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestAnswer.getJoinRequestId())
                .orElseThrow(() -> new NotFoundException(JoinRequest.class, joinRequestAnswer.getJoinRequestId()));

        joinRequest.answerJoinRequest(joinRequestAnswer, userContext.getUserId());

        if (joinRequest.isApproval()) {
            eventPublisher.publishEvent(new JoinRequestEvent(joinRequest));
        }

        String message = getJoinRequestMessage(joinRequest);

        notificationEvent.publish(
                joinRequest.getUser().getUserId(),
                message,
                joinRequest.getUser().getFcmToken(),
                joinRequest.getMeeting().getName().getValue());

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

    @Transactional(readOnly = true)
    public Page<JoinRequest> getMyJoinRequests(Pageable pageable) {
        long count = joinRequestRepository.countByUserId(userContext.getUserId());
        List<JoinRequest> joinRequests = joinRequestRepository.findAllByUserId(userContext.getUserId(), pageable)
                .stream().map(joinRequest -> {
                    joinRequest.mapBlockToRequest();
                    return joinRequest;
                })
                .collect(Collectors.toUnmodifiableList());

        return new PageImpl<>(joinRequests, pageable, count);
    }

    private String getJoinRequestMessage(JoinRequest joinRequest) {
        if (JoinRequestStatus.APPROVAL.equals(joinRequest.getStatus())) {
            return JOIN_APPROVAL_MESSAGE;
        } else if (JoinRequestStatus.REJECT.equals(joinRequest.getStatus())) {
            return JOIN_REJECT_MESSAGE;
        }
        return null;
    }
}
