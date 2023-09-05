package com.assemble.join.repository.impl;

import com.assemble.join.entity.JoinRequest;
import com.assemble.join.entity.QJoinRequest;
import com.assemble.join.repository.JoinRequestCustomRepository;
import com.assemble.meeting.entity.QMeeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class JoinRequestCustomRepositoryImpl implements JoinRequestCustomRepository {

    private JPAQueryFactory queryFactory;

    public JoinRequestCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<JoinRequest> findAllByMeetingId(Long meetingId) {
        return queryFactory.selectFrom(QJoinRequest.joinRequest)
                .leftJoin(QMeeting.meeting)
                .on(QJoinRequest.joinRequest.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QJoinRequest.joinRequest.meeting.isDeleted.isFalse())
                .fetchJoin()
                .where(QJoinRequest.joinRequest.meeting.meetingId.eq(meetingId))
                .orderBy(QJoinRequest.joinRequest.modifiedDate.desc())
                .fetch();
    }

    @Override
    public long countByMeetingId(Long meetingId) {
        return queryFactory.select(QJoinRequest.joinRequest.count())
                .from(QJoinRequest.joinRequest)
                .leftJoin(QMeeting.meeting)
                .on(QJoinRequest.joinRequest.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QMeeting.meeting.isDeleted.isFalse())
                .fetchJoin()
                .where(QMeeting.meeting.meetingId.eq(meetingId))
                .fetchOne();
    }

    @Override
    public List<JoinRequest> findAllByUserId(Long userId, Pageable pageable) {
        return queryFactory.selectFrom(QJoinRequest.joinRequest)
                .where(QJoinRequest.joinRequest.user.userId.eq(userId))
                .orderBy(QJoinRequest.joinRequest.modifiedDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
