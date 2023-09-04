package com.assemble.join.repository.impl;

import com.assemble.join.entity.JoinRequest;
import com.assemble.join.entity.QJoinRequest;
import com.assemble.join.repository.JoinRequestCustomRepository;
import com.assemble.meeting.entity.QMeeting;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class JoinRequestCustomRepositoryImpl implements JoinRequestCustomRepository {

    private JPAQueryFactory queryFactory;

    public JoinRequestCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<JoinRequest> findAllByPostId(Long meetingId) {
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
    public long countByPostId(Long meetingId) {
        return queryFactory.select(QJoinRequest.joinRequest.count())
                .from(QJoinRequest.joinRequest)
                .leftJoin(QMeeting.meeting)
                .on(QJoinRequest.joinRequest.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QMeeting.meeting.isDeleted.isFalse())
                .fetchJoin()
                .where(QMeeting.meeting.meetingId.eq(meetingId))
                .fetchOne();
    }
}
