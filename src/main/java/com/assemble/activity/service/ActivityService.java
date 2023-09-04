package com.assemble.activity.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.meeting.entity.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public Page<Meeting> getActiveAssembles(Pageable pageable) {
        long count = activityRepository.countByActiveAssembles(userContext.getUserId());
        List<Meeting> activityAssembles = activityRepository.findAllByActiveAssembles(userContext.getUserId(), pageable);

        return new PageImpl<>(activityAssembles, pageable, count);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getJoinUserOfAssemble(Long meetingId, Pageable pageable) {
        long count = activityRepository.countByUserOfAssemble(meetingId);
        List<Activity> userOfAssemble = activityRepository.findAllByUserOfAssemble(meetingId, pageable);

        return new PageImpl<>(userOfAssemble, pageable, count);
    }

    @Transactional
    public boolean withdrawJoinAssemble(Long meetingId) {
        Activity activity = activityRepository.findByPostIdAndUserId(meetingId, userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(Activity.class, meetingId, userContext.getUserId()));

        activity.withdraw(userContext.getUserId());

        return true;
    }
}
