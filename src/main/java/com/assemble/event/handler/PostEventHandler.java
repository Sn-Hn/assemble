package com.assemble.event.handler;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.PostEvent;
import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventHandler {

    private final ActivityRepository activityRepository;
    private final UserContext userContext;

    @EventListener
    public Activity doPostEvent(PostEvent postEvent) {
        Post post = postEvent.getPost();
        log.info("Post id={}", post.getPostId());

        Activity activity = new Activity(post, new User(userContext.getUserId()), ActivityStatus.NORMAL);

        Activity savedActivity = activityRepository.save(activity);
        log.info("Saved Join id={}", savedActivity.getId());

        return savedActivity;
    }
}