package com.assemble.schedule.service;

import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.entity.Schedule;
import com.assemble.schedule.repository.ScheduleRepository;
import com.assemble.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public Schedule registerSchdule(Long meetingId, ScheduleCreationRequest scheduleCreationRequest) {
        activityRepository.findByMeetingIdAndUserId(meetingId, AuthenticationUtils.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("모임에서 활동중인 회원이 아닙니다."));

        Schedule schedule = scheduleCreationRequest.toEntity(meetingId);
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findSchedulesByYearAndMonth(Long meetingId, ScheduleYearAndMonthRequest scheduleYearAndMonthRequest) {
        activityRepository.findByMeetingIdAndUserId(meetingId, AuthenticationUtils.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("모임에서 활동중인 회원이 아닙니다."));

        return scheduleRepository.findAllByYearAndMonth(
                meetingId, String.valueOf(scheduleYearAndMonthRequest.getYearAndMonth()));
    }

    @Transactional(readOnly = true)
    public Schedule findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(Schedule.class, scheduleId));

        activityRepository.findByMeetingIdAndUserId(schedule.getMeeting().getMeetingId(), AuthenticationUtils.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("모임에서 활동중인 회원이 아닙니다."));

        return schedule;
    }

    @Transactional
    public Schedule modifySchedule(ModifiedScheduleRequest modifiedScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(modifiedScheduleRequest.getId())
                .orElseThrow(() -> new NotFoundException(Schedule.class, modifiedScheduleRequest.getId()));

        activityRepository.findByMeetingIdAndUserId(schedule.getMeeting().getMeetingId(), AuthenticationUtils.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("모임에서 활동중인 회원이 아닙니다."));

        schedule.modify(modifiedScheduleRequest.getTitle(), modifiedScheduleRequest.getContent());

        return schedule;
    }

    @Transactional
    public boolean deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(Schedule.class, scheduleId));

        activityRepository.findByMeetingIdAndUserId(schedule.getMeeting().getMeetingId(), AuthenticationUtils.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("모임에서 활동중인 회원이 아닙니다."));

        scheduleRepository.deleteById(scheduleId);

        return true;
    }
}
