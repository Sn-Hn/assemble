package com.assemble.schedule.repository;

import com.assemble.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT schedule.* FROM Schedule schedule WHERE schedule.date like :yearAndMonth%",
            nativeQuery = true)
    List<Schedule> findAllByYearAndMonth(@Param("yearAndMonth") String yearAndMonth);
}
