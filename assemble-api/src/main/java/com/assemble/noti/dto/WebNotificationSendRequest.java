package com.assemble.noti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class WebNotificationSendRequest {

    private String userId;

    private String message;

    private String fcmToken;
}
