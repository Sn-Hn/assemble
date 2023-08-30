package com.assemble.joinrequest.repository.impl;

import com.assemble.joinrequest.entity.JoinRequest;
import com.assemble.joinrequest.entity.QJoinRequest;
import com.assemble.joinrequest.repository.JoinRequestCustomRepository;
import com.assemble.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class JoinRequestCustomRepositoryImpl implements JoinRequestCustomRepository {

    private JPAQueryFactory queryFactory;

    public JoinRequestCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<JoinRequest> findAllByPostId(Long postId, Pageable pageable) {
        return queryFactory.selectFrom(QJoinRequest.joinRequest)
                .leftJoin(QPost.post)
                .on(QJoinRequest.joinRequest.post.postId.eq(QPost.post.postId),
                        QJoinRequest.joinRequest.post.isDeleted.isFalse())
                .fetchJoin()
                .where(QJoinRequest.joinRequest.post.postId.eq(postId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QJoinRequest.joinRequest.id.desc())
                .fetch();
    }

    @Override
    public long countByPostId(Long postId) {
        return queryFactory.select(QJoinRequest.joinRequest.count())
                .from(QJoinRequest.joinRequest)
                .leftJoin(QPost.post)
                .on(QJoinRequest.joinRequest.post.postId.eq(QPost.post.postId),
                        QPost.post.isDeleted.isFalse())
                .fetchJoin()
                .where(QPost.post.postId.eq(postId))
                .fetchOne();
    }
}
