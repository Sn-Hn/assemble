package com.assemble.schedule.fixture;

import com.assemble.schedule.dto.request.ModifiedScheduleRequest;
import com.assemble.schedule.dto.request.ScheduleCreationRequest;
import com.assemble.schedule.entity.Schedule;
import com.assemble.user.fixture.UserFixture;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleFixture {

    public static Schedule 삭제_일정() {
        return new Schedule(
                id,
                title,
                content,
                UserFixture.회원(),
                dateOctober,
                notDeleted
        );
    }

    public static Schedule 일정_9월() {
        Schedule schedule = new Schedule(
                id,
                title,
                content,
                UserFixture.회원(),
                dateSeptember,
                notDeleted
        );
        schedule.setCreatedDate(LocalDateTime.now());
        return schedule;
    }

    public static ScheduleCreationRequest 일정_생성_요청() {
        return new ScheduleCreationRequest(
                title,
                content,
                dateOctober
        );
    }

    public static ModifiedScheduleRequest 일정_변경_요청() {
        return new ModifiedScheduleRequest(
                id,
                "제목 변경 요청",
                "내용 변경 요청"
        );
    }

    private static final Long id = 1L;
    private static final Long deleteId = 2L;
    private static final String title = "모임일정 제목";
    private static final String content = "10월 20일 아침 10시 강남역에서 만나요~";
    private static final LocalDate dateOctober = LocalDate.of(2023, 10, 14);
    private static final LocalDate dateSeptember = LocalDate.of(2023, 9, 14);
    private static final boolean notDeleted = false;

}
