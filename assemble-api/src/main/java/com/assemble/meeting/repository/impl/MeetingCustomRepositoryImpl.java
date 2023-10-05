package com.assemble.meeting.repository.impl;

import com.assemble.meeting.domain.MeetingOrderByType;
import com.assemble.meeting.domain.MeetingSearchType;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.entity.QLikes;
import com.assemble.meeting.entity.QMeeting;
import com.assemble.meeting.repository.MeetingCustomRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingCustomRepositoryImpl implements MeetingCustomRepository {

    private JPAQueryFactory queryFactory;

    public MeetingCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Meeting> findAllBySearch(MeetingSearchRequest meetingSearchRequest, Long myUserId, Pageable pageable) {
        return queryFactory.select(QMeeting.meeting, QLikes.likes)
                .from(QMeeting.meeting)
                .leftJoin(QLikes.likes)
                .on(QMeeting.meeting.meetingId.eq(QLikes.likes.meeting.meetingId),
                        eqLikeUserId(myUserId))
                .where(searchByLike(meetingSearchRequest.getSearchBy(), meetingSearchRequest.getSearchQuery()),
                        searchByCategory(meetingSearchRequest.getCategoryId()))
                .orderBy(findOrder(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    Meeting meeting = tuple.get(QMeeting.meeting);
                    Likes likes = tuple.get(QLikes.likes);

                    setIsLike(meeting, likes);

                    return meeting;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private static void setIsLike(Meeting meeting, Likes likes) {
        if (meeting != null && likes != null) {
            meeting.setIsLike(true);
        }
    }

    @Override
    public List<Meeting> findAllByUserId(Long userId, Long myUserId, Pageable pageable) {
        return queryFactory.select(QMeeting.meeting, QLikes.likes)
                .from(QMeeting.meeting)
                .leftJoin(QLikes.likes)
                .on(QMeeting.meeting.meetingId.eq(QLikes.likes.meeting.meetingId),
                        eqLikeUserId(myUserId))
                .where(eqUserId(userId))
                .orderBy(QMeeting.meeting.meetingId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin().fetch()
                .stream()
                .map(tuple -> {
                    Meeting meeting = tuple.get(QMeeting.meeting);
                    Likes likes = tuple.get(QLikes.likes);

                    setIsLike(meeting, likes);

                    return meeting;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public long countByUserId(Long userId) {
        return queryFactory.select(QMeeting.meeting.count())
                .from(QMeeting.meeting)
                .where(eqUserId(userId))
                .fetchOne();
    }

    @Override
    public long countBySearch(MeetingSearchRequest meetingSearchRequest) {
        return queryFactory.select(QMeeting.meeting.count())
                .from(QMeeting.meeting)
                .where(searchByLike(meetingSearchRequest.getSearchBy(), meetingSearchRequest.getSearchQuery()),
                        searchByCategory(meetingSearchRequest.getCategoryId()))
                .fetchOne();
    }

    private BooleanExpression eqUserId(Long userId) {
        return QMeeting.meeting.user.userId.eq(userId);
    }

    private static BooleanExpression eqLikeUserId(Long userId) {
        return QLikes.likes.user.userId.eq(userId);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (!StringUtils.hasText(searchBy) || !StringUtils.hasText(searchQuery)) {
            return null;
        }

        return MeetingSearchType.findPostSearchType(searchBy, searchQuery);
    }

    private Predicate searchByCategory(Long categoryId) {
        return categoryId != null ? QMeeting.meeting.category.id.eq(categoryId) : null;
    }

    private OrderSpecifier<?> findOrder(Pageable pageable) {
        Sort.Order order = pageable.getSort().get().findFirst().orElseGet(() -> new Sort.Order(Sort.Direction.DESC, "total"));

        return MeetingOrderByType.findPostOrderQuery(order.getProperty());
    }
}
