package com.assemble.event.handler;

import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.join.entity.JoinRequest;
import com.assemble.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinRequestEventHandler {

    private final ActivityRepository activityRepository;

    @EventListener
    public Activity doJoinRequestEvent(JoinRequestEvent joinRequestEvent) {
        JoinRequest joinRequest = joinRequestEvent.getJoinRequest();
        log.info("JoinRequest id={}", joinRequest.getId());

        Activity activity = new Activity(joinRequest.getMeeting(), new User(joinRequest.getUser().getUserId()), ActivityStatus.NORMAL);

        Activity savedActivity = activityRepository.save(activity);
        log.info("Saved Join id={}", savedActivity.getId());

        return savedActivity;
    }
}
