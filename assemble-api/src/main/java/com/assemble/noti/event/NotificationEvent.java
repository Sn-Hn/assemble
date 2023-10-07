package com.assemble.noti.event;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.assemble.noti.dto.WebNotificationRequest;
import com.assemble.util.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationEvent {

    @Value("${cloud.aws.sqs.queue-name}")
    private String QUEUE_NAME;

    private final QueueMessagingTemplate queueMessagingTemplate;

    public NotificationEvent(AmazonSQS amazonSQS) {
        this.queueMessagingTemplate = new QueueMessagingTemplate((AmazonSQSAsync) amazonSQS);
    }

    public void publish(String message, Object... args) {
        Message<String> sendMessage = MessageBuilder.withPayload(MessageUtils.getMessage(message, args)).build();
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, sendMessage);
    }

    public void publish(Long userId, String message, String fcmToken, Object... args) {
        WebNotificationRequest webNotificationRequest =
                new WebNotificationRequest(String.valueOf(userId), MessageUtils.getMessage(message, args), fcmToken);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, webNotificationRequest);
    }

    public void publish(Long userId, String message, String fcmToken) {
        WebNotificationRequest webNotificationRequest =
                new WebNotificationRequest(String.valueOf(userId), message, fcmToken);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, webNotificationRequest);
    }
}
