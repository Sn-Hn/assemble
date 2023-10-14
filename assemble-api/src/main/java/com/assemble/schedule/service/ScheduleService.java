package com.assemble.schedule.service;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonth;
import com.assemble.schedule.entity.Schedule;
import com.assemble.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule registerSchdule(ScheduleCreationRequest scheduleCreationRequest) {
        Schedule schedule = scheduleCreationRequest.toEntity();
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesByYearAndMonth(ScheduleYearAndMonth scheduleYearAndMonth) {
        return scheduleRepository.findAllByYearAndMonth(scheduleYearAndMonth.getYearAndMonth());
    }

    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(Schedule.class, scheduleId));
    }

    public Schedule modifySchedule(ModifiedScheduleRequest modifiedScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(modifiedScheduleRequest.getId())
                .orElseThrow(() -> new NotFoundException(Schedule.class, modifiedScheduleRequest.getId()));

        schedule.modify(modifiedScheduleRequest.getTitle(), modifiedScheduleRequest.getContent());

        return schedule;
    }

    public boolean deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);

        return true;
    }
}
