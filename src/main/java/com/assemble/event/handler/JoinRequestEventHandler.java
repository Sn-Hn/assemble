package com.assemble.event.handler;

import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.join.domain.JoinStatus;
import com.assemble.join.entity.Join;
import com.assemble.join.repository.JoinRepository;
import com.assemble.joinrequest.entity.JoinRequest;
import com.assemble.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinRequestEventHandler {

    private final JoinRepository joinRepository;

    @EventListener
    public Join doJoinRequestEvent(JoinRequestEvent joinRequestEvent) {
        JoinRequest joinRequest = joinRequestEvent.getJoinRequest();
        log.info("JoinRequest id={}", joinRequest.getId());

        Join join = new Join(joinRequest.getPost(), new User(joinRequest.getUser().getUserId()), JoinStatus.NORMAL);

        Join savedJoin = joinRepository.save(join);
        log.info("Saved Join id={}", savedJoin.getId());

        return savedJoin;
    }
}
