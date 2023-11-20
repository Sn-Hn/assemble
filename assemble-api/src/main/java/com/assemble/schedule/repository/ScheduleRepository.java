package com.assemble.schedule.repository;

import com.assemble.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT schedule.* FROM schedule schedule " +
            "WHERE DATE_FORMAT(schedule.date, '%Y-%m') = :yearAndMonth " +
            "AND schedule.meeting_id = :meetingId " +
            "AND schedule.is_deleted = 'N'",
            nativeQuery = true)
    List<Schedule> findAllByYearAndMonth(@Param("meetingId") Long meetingId,
                                         @Param("yearAndMonth") String yearAndMonth);
}
