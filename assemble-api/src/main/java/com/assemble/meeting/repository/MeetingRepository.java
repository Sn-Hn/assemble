package com.assemble.meeting.repository;

import com.assemble.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingCustomRepository {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Meeting meeting SET meeting.hits = meeting.hits + 1 WHERE meeting.meetingId = :meetingId")
    void increaseHits(@Param(value = "meetingId") Long meetingId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Meeting meeting SET meeting.likes = meeting.likes + 1 WHERE meeting.meetingId = :meetingId")
    void increaseLikes(@Param(value = "meetingId")Long meetingId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Meeting meeting SET meeting.likes = meeting.likes - 1 WHERE meeting.meetingId = :meetingId")
    void decreaseLikes(@Param(value = "meetingId")Long meetingId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT meeting FROM Meeting meeting WHERE meeting.meetingId = :meetingId")
    Optional<Meeting> findByIdForUpdate(@Param(value = "meetingId") Long meetingId);
}
