package com.assemble.noti.service;

import com.assemble.noti.dto.WebNotificationSendRequest;
import com.assemble.noti.entity.Notification;
import com.assemble.noti.event.NotificationPublishEvent;
import com.assemble.noti.repository.NotificationRepository;
import com.assemble.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPublishEvent notificationPublishEvent;

    @Transactional
    public Notification createWebNotification(WebNotificationSendRequest webNotificationSendRequest) {
        Long userId = Long.valueOf(webNotificationSendRequest.getUserId());
        Notification notification = new Notification(new User(userId), webNotificationSendRequest.getMessage());

        notificationPublishEvent.publish(webNotificationSendRequest);

        Notification savedNotification = notificationRepository.save(notification);

        return savedNotification;
    }
}
