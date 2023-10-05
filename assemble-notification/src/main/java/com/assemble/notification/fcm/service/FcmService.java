package com.assemble.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmService {

    public void sendNotification(Long targetUserId, String message, String token) {
        Message msg = Message.builder()
                .putData("targetUserId", String.valueOf(targetUserId))
                .putData("message", message)
                .setToken(token)
                .build();

        FirebaseMessaging.getInstance().sendAsync(msg);
    }
}
