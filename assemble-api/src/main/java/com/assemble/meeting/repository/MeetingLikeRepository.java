package com.assemble.meeting.repository;

import com.assemble.meeting.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingLikeRepository extends JpaRepository<Likes, Long>, MeetingLikeCustomRepository {
}
