package com.assemble.event.handler;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.event.publish.JoinRequestSendNotificationEvent;
import com.assemble.join.entity.JoinRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.repository.MeetingRepository;
import com.assemble.noti.dto.WebNotificationSendRequest;
import com.assemble.noti.entity.Notification;
import com.assemble.noti.service.NotificationService;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import com.assemble.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinRequestSendNotificationEventHandler {

    private final String JOIN_REQUEST_MESSAGE = "web.notification.message.join.request";

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    @EventListener
    public Notification joinRequestNotificationEvent(JoinRequestSendNotificationEvent joinRequestSendNotificationEvent) {
        JoinRequest joinRequest = joinRequestSendNotificationEvent.getJoinRequest();
        User user = userRepository.findById(joinRequest.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException(User.class));
        log.info("모임 가입 신청 회원 ID={}, Nickname={}", user.getUserId(), user.getNickname());

        Meeting meeting = meetingRepository.findById(joinRequest.getMeeting().getMeetingId())
                .orElseThrow(() -> new NotFoundException(Meeting.class));

        Long targetUserId = meeting.getUser().getUserId();
        log.info("알림 받을 대상 회원 ID={}", targetUserId);

        String sendMessage = MessageUtils.getMessage(JOIN_REQUEST_MESSAGE,
                user.getNickname(), meeting.getName().getValue());

        log.info("Notification Message={}", sendMessage);

        return notificationService.createWebNotification(
                new WebNotificationSendRequest(String.valueOf(targetUserId), sendMessage, meeting.getUser().getFcmToken())
        );
    }
}
