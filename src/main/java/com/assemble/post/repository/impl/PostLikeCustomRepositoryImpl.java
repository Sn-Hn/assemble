package com.assemble.post.repository.impl;

import com.assemble.post.entity.Likes;
import com.assemble.post.entity.QLikes;
import com.assemble.post.entity.QPost;
import com.assemble.post.repository.PostLikeCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Likes> findPostByUser(Long postId, Long myUserId) {
        return jpaQueryFactory.selectFrom(QLikes.likes)
                .where(searchByUserId(myUserId),
                        searchByPostId(postId))
                .fetch()
                .stream().findFirst();
    }

    @Override
    public List<Likes> findAllByUserId(Long userId, Pageable pageable) {
        return jpaQueryFactory.selectFrom(QLikes.likes)
                .innerJoin(QPost.post)
                .on(QLikes.likes.post.postId.eq(QPost.post.postId),
                        QPost.post.isDeleted.isFalse())
                .fetchJoin()
                .where(searchByUserId(userId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QLikes.likes.post.modifiedDate.desc())
                .fetch();
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaQueryFactory.select(QLikes.likes.count())
                .from(QLikes.likes)
                .where(searchByUserId(userId),
                        QPost.post.isDeleted.isFalse())
                .fetchOne();
    }

    private BooleanExpression searchByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        return QLikes.likes.user.userId.eq(userId);
    }

    private BooleanExpression searchByPostId(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("postId is null");
        }

        return QLikes.likes.post.postId.eq(postId);
    }
}
