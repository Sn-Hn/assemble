package com.assemble.notification.sqs.listener;

import com.assemble.noti.dto.WebNotificationRequest;
import com.assemble.notification.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final FcmService fcmService;

    @SqsListener(value = "assemble-web-notification-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void messageListener(@Payload WebNotificationRequest webNotificationRequest, @Headers Map<String, String> headers) {
        log.info("webNotificationRequest: {}", webNotificationRequest);
        log.info("headers: {}", headers);

        Long userId = Long.valueOf(webNotificationRequest.getUserId());

        fcmService.sendNotification(userId, webNotificationRequest.getMessage(), webNotificationRequest.getFcmToken());
    }
}