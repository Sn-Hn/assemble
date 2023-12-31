package com.assemble.schedule.service;

import com.assemble.activity.fixture.ActivityFixture;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.dto.request.ScheduleYearAndMonthRequest;
import com.assemble.schedule.entity.Schedule;
import com.assemble.schedule.fixture.ScheduleFixture;
import com.assemble.schedule.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@DisplayName("Schedule Service Test")
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Test
    void 일정_등록_검증() {
        // given
        Long meetingId = 1L;
        ScheduleCreationRequest scheduleCreationRequest = ScheduleFixture.일정_생성_요청();
        given(scheduleRepository.save(any())).willReturn(scheduleCreationRequest.toEntity(meetingId));
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(ActivityFixture.특정_모임_활동_중인_회원()));

        // when
        Schedule schedule = scheduleService.registerSchdule(meetingId, scheduleCreationRequest);

        // then
        assertAll(
                () -> assertThat(schedule).isNotNull(),
                () -> assertThat(schedule.getTitle()).isEqualTo(scheduleCreationRequest.getTitle()),
                () -> assertThat(schedule.getContent()).isEqualTo(scheduleCreationRequest.getContent()),
                () -> assertThat(schedule.getDate()).isEqualTo(scheduleCreationRequest.getDate())
        );
    }

    @Test
    void 일정_목록_연월_조회_검증() {
        // given
        Long meetingId = 1L;
        String yearAndMonth = "2023-09";
        ScheduleYearAndMonthRequest request = new ScheduleYearAndMonthRequest(yearAndMonth);
        Schedule sepSchedule = ScheduleFixture.일정_9월();
        given(scheduleRepository.findAllByYearAndMonth(anyLong(), any())).willReturn(List.of(sepSchedule));
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(ActivityFixture.특정_모임_활동_중인_회원()));

        // when
        List<Schedule> schedulesByYearAndMonth = scheduleService.findSchedulesByYearAndMonth(meetingId, request);

        // then
        assertAll(
                () -> assertThat(schedulesByYearAndMonth.size()).isGreaterThan(0),
                () -> assertThat(schedulesByYearAndMonth.stream()
                        .findAny()
                        .get()
                        .getDate().format(DateTimeFormatter.ISO_DATE)).contains(yearAndMonth)
        );
    }

    @Test
    void 일정_상세_조회_검증() {
        // given
        Long id = 1L;
        given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(ScheduleFixture.일정_9월()));
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(ActivityFixture.특정_모임_활동_중인_회원()));

        // when
        Schedule schedule = scheduleService.findScheduleById(id);

        // then
        assertThat(schedule.getId()).isEqualTo(id);
    }

    @Test
    void 일정_변경_검증() {
        // given
        Long id = 1L;
        ModifiedScheduleRequest modifiedScheduleRequest = ScheduleFixture.일정_변경_요청();
        given(scheduleRepository.findById(id)).willReturn(Optional.of(ScheduleFixture.일정_9월()));
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(ActivityFixture.특정_모임_활동_중인_회원()));

        // when
        Schedule modifiedSchedule = scheduleService.modifySchedule(modifiedScheduleRequest);

        // then
        assertAll(
                () -> assertThat(modifiedSchedule.getTitle()).isEqualTo(modifiedSchedule.getTitle()),
                () -> assertThat(modifiedSchedule.getContent()).isEqualTo(modifiedSchedule.getContent())
        );
    }
    
    @Test
    void 일정_삭제_검증() {
        // given
        Long deleteId = 3L;
        given(scheduleRepository.findById(deleteId)).willReturn(Optional.of(ScheduleFixture.일정_9월()));
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(ActivityFixture.특정_모임_활동_중인_회원()));

        // when
        boolean isDeleted = scheduleService.deleteSchedule(deleteId);

        // then
        assertThat(isDeleted).isTrue();
    }
}