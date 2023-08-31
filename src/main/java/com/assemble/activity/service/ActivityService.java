package com.assemble.activity.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: 2023-08-31 오늘 단위, 통합테스트 작성하기 -신한
// TODO: 2023-08-31 관리자 체킹 AOP 작성하기 -신한
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public Page<Post> getActiveAssembles(Pageable pageable) {
        long count = activityRepository.countByActiveAssembles(userContext.getUserId());
        List<Post> activityAssembles = activityRepository.findAllByActiveAssembles(userContext.getUserId(), pageable);

        return new PageImpl<>(activityAssembles, pageable, count);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getJoinUserOfAssemble(Long postId, Pageable pageable) {
        long count = activityRepository.countByUserOfAssemble(postId);
        List<Activity> userOfAssemble = activityRepository.findAllByUserOfAssemble(postId, pageable);

        return new PageImpl<>(userOfAssemble, pageable, count);
    }

    @Transactional
    public boolean withdrawJoinAssemble(Long postId) {
        Activity activity = activityRepository.findByPostIdAndUserId(postId, userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(Activity.class, postId, userContext.getUserId()));

        activity.withdraw(userContext.getUserId());

        return true;
    }
}
