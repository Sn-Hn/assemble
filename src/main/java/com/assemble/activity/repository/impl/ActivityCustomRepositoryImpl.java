package com.assemble.activity.repository.impl;

import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.entity.QActivity;
import com.assemble.activity.repository.ActivityCustomRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.entity.QMeeting;
import com.assemble.user.domain.UserStatus;
import com.assemble.user.entity.QUser;
import com.assemble.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {

    private JPAQueryFactory queryFactory;

    public ActivityCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long countByActiveAssembles(Long userId) {
        return queryFactory.select(QActivity.activity.count())
                .from(QActivity.activity)
                .innerJoin(QActivity.activity.user, QUser.user)
                .where(QActivity.activity.user.userId.eq(userId),
                        isNotWithdrawalAssemble(),
                        isNotWithdrawalUser())
                .fetchOne();
    }

    @Override
    public List<Meeting> findAllByActiveAssembles(Long userId, Pageable pageable) {
        return queryFactory.select(QActivity.activity.meeting)
                .from(QActivity.activity)
                .innerJoin(QActivity.activity.user, QUser.user)
                .where(QActivity.activity.user.userId.eq(userId),
                        isNotWithdrawalAssemble(),
                        isNotWithdrawalUser())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QActivity.activity.createdDate.desc())
                .fetch();
    }

    @Override
    public long countByUserOfAssemble(Long meetingId) {
        return queryFactory.select(QActivity.activity.count())
                .from(QActivity.activity)
                .innerJoin(QActivity.activity.user, QUser.user)
                .where(QActivity.activity.meeting.meetingId.eq(meetingId),
                        isNotWithdrawalAssemble(),
                        isNotWithdrawalUser())
                .fetchOne();
    }

    @Override
    public List<Activity> findAllByUserOfAssemble(Long meetingId, Pageable pageable) {
        return queryFactory.selectFrom(QActivity.activity)
                .innerJoin(QActivity.activity.user, QUser.user)
                .fetchJoin()
                .innerJoin(QActivity.activity.meeting, QMeeting.meeting)
                .fetchJoin()
                .where(QActivity.activity.meeting.meetingId.eq(meetingId),
                        isNotWithdrawalAssemble(),
                        isNotWithdrawalUser())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QActivity.activity.user.nickname.asc())
                .fetch()
                .stream()
                .map(join -> {
                    Meeting meeting = join.getMeeting();
                    User user = join.getUser();

                    if (meeting != null && user != null && meeting.getUser().getUserId().equals(user.getUserId())) {
                        join.setIsHost(true);
                    }

                    return join;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private BooleanExpression isNotWithdrawalAssemble() {
        return QActivity.activity.status.eq(ActivityStatus.NORMAL);
    }

    private BooleanExpression isNotWithdrawalUser() {
        return QActivity.activity.user.status.notIn(UserStatus.WITHDRAWAL);
    }
}
