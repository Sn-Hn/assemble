package com.assemble.event.handler;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.PostEvent;
import com.assemble.join.domain.JoinStatus;
import com.assemble.join.entity.Join;
import com.assemble.join.repository.JoinRepository;
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

    private final JoinRepository joinRepository;
    private final UserContext userContext;

    @EventListener
    public Join doPostEvent(PostEvent postEvent) {
        Post post = postEvent.getPost();
        log.info("Post id={}", post.getPostId());

        Join join = new Join(post, new User(userContext.getUserId()), JoinStatus.NORMAL);

        Join savedJoin = joinRepository.save(join);
        log.info("Saved Join id={}", savedJoin.getId());

        return savedJoin;
    }
}
