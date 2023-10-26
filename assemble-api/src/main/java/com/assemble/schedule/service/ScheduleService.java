package com.assemble.schedule.service;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.entity.Schedule;
import com.assemble.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule registerSchdule(ScheduleCreationRequest scheduleCreationRequest) {
        Schedule schedule = scheduleCreationRequest.toEntity();
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findSchedulesByYearAndMonth(ScheduleYearAndMonthRequest scheduleYearAndMonthRequest) {
        return scheduleRepository.findAllByYearAndMonth(String.valueOf(scheduleYearAndMonthRequest.getYearAndMonth()));
    }

    @Transactional(readOnly = true)
    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(Schedule.class, scheduleId));
    }

    @Transactional
    public Schedule modifySchedule(ModifiedScheduleRequest modifiedScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(modifiedScheduleRequest.getId())
                .orElseThrow(() -> new NotFoundException(Schedule.class, modifiedScheduleRequest.getId()));

        schedule.modify(modifiedScheduleRequest.getTitle(), modifiedScheduleRequest.getContent());

        return schedule;
    }

    @Transactional
    public boolean deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);

        return true;
    }
}
