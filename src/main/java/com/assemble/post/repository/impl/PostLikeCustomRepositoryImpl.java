package com.assemble.post.repository.impl;

import com.assemble.post.repository.PostLikeCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


}
