package com.assemble.activity.repository;

import com.assemble.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityCustomRepository {

    @Query(value = "SELECT activity FROM Activity activity " +
            "WHERE activity.meeting.meetingId = :meetingId " +
            "AND activity.user.userId = :userId " +
            "AND activity.status not in (com.assemble.activity.domain.ActivityStatus.WITHDRAWAL, " +
            "com.assemble.activity.domain.ActivityStatus.DISMISS)")
    Optional<Activity> findByMeetingIdAndUserId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
    
    @Query(value = "SELECT activity FROM Activity activity " +
            "WHERE activity.meeting.meetingId = :meetingId " +
            "AND activity.status not in (com.assemble.activity.domain.ActivityStatus.WITHDRAWAL, " +
            "com.assemble.activity.domain.ActivityStatus.DISMISS)")
    List<Activity> findByMeetingId(@Param("meetingId") Long meetingId);
    @Query(value = "SELECT count(activity) FROM Activity activity " +
            "WHERE activity.meeting.meetingId = :meetingId " +
            "AND activity.status not in (com.assemble.activity.domain.ActivityStatus.WITHDRAWAL, " +
            "com.assemble.activity.domain.ActivityStatus.DISMISS)")
    long countByMeetingId(@Param("meetingId") Long meetingId);

}
