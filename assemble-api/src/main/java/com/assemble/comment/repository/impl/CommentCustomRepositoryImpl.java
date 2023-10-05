package com.assemble.comment.repository.impl;

import com.assemble.comment.entity.Comment;
import com.assemble.comment.entity.QComment;
import com.assemble.comment.repository.CommentCustomRepository;
import com.assemble.meeting.entity.QMeeting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private JPAQueryFactory queryFactory;

    public CommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findByUser(Long userId, Pageable pageable) {
        return queryFactory.selectFrom(QComment.comment)
                .innerJoin(QMeeting.meeting)
                .on(QComment.comment.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QMeeting.meeting.isDeleted.isFalse())
                .where(isCommentsByUserId(userId))
                .orderBy(QComment.comment.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public long countByUserId(Long userId) {
        return queryFactory.select(QComment.comment.count())
                .from(QComment.comment)
                .innerJoin(QMeeting.meeting)
                .on(QComment.comment.meeting.meetingId.eq(QMeeting.meeting.meetingId),
                        QMeeting.meeting.isDeleted.isFalse())
                .where(isCommentsByUserId(userId))
                .fetchOne();
    }

    private BooleanExpression isCommentsByUserId(Long userId) {
        return QComment.comment.user.userId.eq(userId);
    }

}
