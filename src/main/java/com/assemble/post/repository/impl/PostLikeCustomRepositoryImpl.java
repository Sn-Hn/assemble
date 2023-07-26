package com.assemble.post.repository.impl;

import com.assemble.commons.base.BaseRequest;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.QLikes;
import com.assemble.post.repository.PostLikeCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Likes> findPostByUser(PostLikeRequest postLikeRequest) {
        return jpaQueryFactory.selectFrom(QLikes.likes)
                .where(searchByUserId(BaseRequest.getUserId()),
                        searchByPostId(postLikeRequest.getPostId()))
                .fetch()
                .stream().findFirst();
    }

    private BooleanExpression searchByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException();
        }

        return QLikes.likes.user.userId.eq(userId);
    }

    private BooleanExpression searchByPostId(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException();
        }

        return QLikes.likes.post.postId.eq(postId);
    }
}
