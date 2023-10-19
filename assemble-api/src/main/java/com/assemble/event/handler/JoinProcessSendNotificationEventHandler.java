package com.assemble.event.handler;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.event.publish.JoinProcessSendNotificationEvent;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.entity.JoinRequest;
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
public class JoinProcessSendNotificationEventHandler {

    private final String JOIN_APPROVAL_MESSAGE = "web.notification.message.join.approval";
    private final String JOIN_REJECT_MESSAGE = "web.notification.message.join.reject";
    private final NotificationService notificationService;

    @EventListener
    public Notification joinProcessNotificationEvent(JoinProcessSendNotificationEvent joinProcessSendNotificationEvent) {
        JoinRequest joinRequest = joinProcessSendNotificationEvent.getJoinRequest();
        User user = joinRequest.getUser();
        log.info("모임 가입 신청 회원이자 알림 받을 대상 회원 ID={}, Nickname={}", user.getUserId(), user.getNickname());

        Long targetUserId = joinRequest.getUser().getUserId();

        String meetingName = joinRequest.getMeeting().getName().getValue();

        String message = choiceMessage(joinRequest.getStatus());
        String sendMessage = MessageUtils.getMessage(message, meetingName);

        log.info("Notification Message={}", sendMessage);

        return notificationService.createWebNotification(
                new WebNotificationSendRequest(String.valueOf(targetUserId), sendMessage, user.getFcmToken())
        );
    }

    private String choiceMessage(JoinRequestStatus status) {
        if (JoinRequestStatus.APPROVAL.equals(status)) {
            return JOIN_APPROVAL_MESSAGE;
        }

        if (JoinRequestStatus.REJECT.equals(status)) {
            return JOIN_REJECT_MESSAGE;
        }

        return null;
    }
}
