package com.assemble.schedule.repository;

import com.assemble.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT schedule.* FROM Schedule schedule " +
            "WHERE DATE_FORMAT(schedule.start_date, '%Y-%m') >= :yearAndMonth " +
            "and DATE_FORMAT(schedule.end_date, '%Y-%m') <= :yearAndMonth",
            nativeQuery = true)
    List<Schedule> findAllByYearAndMonth(@Param("yearAndMonth") String yearAndMonth);
}
