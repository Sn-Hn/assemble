package com.assemble.post.repository.impl;

import com.assemble.post.domain.PostSearchType;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import com.assemble.post.entity.QPost;
import com.assemble.post.repository.PostCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import javax.persistence.EntityManager;
import java.util.List;

public class PostCustomRepositoryImpl implements PostCustomRepository {

    private JPAQueryFactory queryFactory;

    public PostCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> findAllBySearch(PostSearchRequest postSearchRequest, Pageable pageable) {
        List<Post> posts = queryFactory.selectFrom(QPost.post)
                .where(searchByLike(postSearchRequest.getSearchBy(), postSearchRequest.getSearchQuery()))
                .orderBy(QPost.post.postId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(posts, pageable, posts.size());
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        return PostSearchType.findPostSearchType(searchBy, searchQuery);
    }

}
