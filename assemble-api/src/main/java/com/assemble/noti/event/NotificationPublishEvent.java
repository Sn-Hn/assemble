package com.assemble.noti.event;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.assemble.noti.dto.WebNotificationSendRequest;
import com.assemble.util.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublishEvent {

    @Value("${cloud.aws.sqs.queue-name}")
    private String QUEUE_NAME;

    private final QueueMessagingTemplate queueMessagingTemplate;

    public NotificationPublishEvent(AmazonSQS amazonSQS) {
        this.queueMessagingTemplate = new QueueMessagingTemplate((AmazonSQSAsync) amazonSQS);
    }

    public void publish(String message, Object... args) {
        Message<String> sendMessage = MessageBuilder.withPayload(MessageUtils.getMessage(message, args)).build();
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, sendMessage);
    }

    public void publish(Long userId, String message, String fcmToken, Object... args) {
        WebNotificationSendRequest webNotificationSendRequest =
                new WebNotificationSendRequest(String.valueOf(userId), MessageUtils.getMessage(message, args), fcmToken);
        publish(webNotificationSendRequest);
    }

    public void publish(Long userId, String fcmToken, String message) {
        WebNotificationSendRequest webNotificationSendRequest =
                new WebNotificationSendRequest(String.valueOf(userId), message, fcmToken);
        publish(webNotificationSendRequest);
    }

    public void publish(WebNotificationSendRequest webNotificationSendRequest) {
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, webNotificationSendRequest);
    }
}
