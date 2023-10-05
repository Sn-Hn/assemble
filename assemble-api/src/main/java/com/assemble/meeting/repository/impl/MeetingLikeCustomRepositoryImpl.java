package com.assemble.meeting.repository.impl;

import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.QLikes;
import com.assemble.meeting.entity.QMeeting;
import com.assemble.meeting.repository.MeetingLikeCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MeetingLikeCustomRepositoryImpl implements MeetingLikeCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    public MeetingLikeCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Likes> findPostByUser(Long meetingId, Long myUserId) {
        return jpaQueryFactory.selectFrom(QLikes.likes)
                .where(searchByUserId(myUserId),
                        searchByMeetingId(meetingId))
                .fetch()
                .stream().findFirst();
    }

    @Override
    public List<Likes> findAllByUserId(Long userId, Pageable pageable) {
        return jpaQueryFactory.selectFrom(QLikes.likes)
                .innerJoin(QMeeting.meeting)
                .on(QLikes.likes.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QMeeting.meeting.isDeleted.isFalse())
                .fetchJoin()
                .where(searchByUserId(userId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QLikes.likes.meeting.modifiedDate.desc())
                .fetch();
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaQueryFactory.select(QLikes.likes.count())
                .from(QLikes.likes)
                .where(searchByUserId(userId),
                        QMeeting.meeting.isDeleted.isFalse())
                .fetchOne();
    }

    private BooleanExpression searchByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        return QLikes.likes.user.userId.eq(userId);
    }

    private BooleanExpression searchByMeetingId(Long meetingId) {
        if (meetingId == null) {
            throw new IllegalArgumentException("meetingId is null");
        }

        return QLikes.likes.meeting.meetingId.eq(meetingId);
    }
}
