package com.assemble.post.repository.impl;

import com.assemble.commons.base.BaseRequest;
import com.assemble.post.domain.PostOrderType;
import com.assemble.post.domain.PostSearchType;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.Post;
import com.assemble.post.entity.QLikes;
import com.assemble.post.entity.QPost;
import com.assemble.post.repository.PostCustomRepository;
import com.assemble.user.entity.QUser;
import com.assemble.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class PostCustomRepositoryImpl implements PostCustomRepository {

    private JPAQueryFactory queryFactory;

    public PostCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> findAllBySearch(PostSearchRequest postSearchRequest, Long myUserId, Pageable pageable, long count) {
        List<Post> posts = getPosts(postSearchRequest, myUserId, pageable);

        return new PageImpl<>(posts, pageable, count);
    }

    @Override
    public Page<Post> findAllByUserId(Long userId, Long myUserId, Pageable pageable, long count) {
        List<Post> postsByUserId = getPostsByUserId(userId, myUserId, pageable);

        return new PageImpl<>(postsByUserId, pageable, count);
    }

    @Override
    public long countByUserId(Long userId) {
        return queryFactory.select(QPost.post.count())
                .from(QPost.post)
                .where(eqUserId(userId))
                .fetchOne();
    }

    @Override
    public long countBySearch(PostSearchRequest postSearchRequest) {
        return queryFactory.select(QPost.post.count())
                .from(QPost.post)
                .where(searchByLike(postSearchRequest.getSearchBy(), postSearchRequest.getSearchQuery()),
                        searchByCategory(postSearchRequest.getCategoryId()))
                .fetchOne();
    }

    private List<Post> getPosts(PostSearchRequest postSearchRequest, Long myUserId, Pageable pageable) {
        return queryFactory.select(QPost.post, QLikes.likes)
                .from(QPost.post)
                .leftJoin(QLikes.likes)
                .on(eqLikeUserId(myUserId), eqPostId())
                .where(searchByLike(postSearchRequest.getSearchBy(), postSearchRequest.getSearchQuery()),
                        searchByCategory(postSearchRequest.getCategoryId()))
                .orderBy(findOrder(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    Post post = tuple.get(QPost.post);
                    Likes likes = tuple.get(QLikes.likes);

                    if (post != null && likes != null) {
                        post.setIsLike(true);
                    }

                    return post;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Post> getPostsByUserId(Long userId, Long myUserId, Pageable pageable) {
        return queryFactory.select(QPost.post, QLikes.likes)
                .from(QPost.post)
                .leftJoin(QLikes.likes)
                .on(eqLikeUserId(myUserId), eqPostId())
                .where(eqUserId(userId))
                .orderBy(QPost.post.postId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    Post post = tuple.get(QPost.post);
                    Likes likes = tuple.get(QLikes.likes);

                    if (post != null && likes != null) {
                        post.setIsLike(true);
                    }

                    return post;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private BooleanExpression eqUserId(Long userId) {
        return QPost.post.user.userId.eq(userId);
    }

    private static BooleanExpression eqPostId() {
        return QPost.post.postId.eq(QLikes.likes.post.postId);
    }

    private static BooleanExpression eqLikeUserId(Long userId) {
        return QLikes.likes.user.userId.eq(userId);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (!StringUtils.hasText(searchBy) || !StringUtils.hasText(searchQuery)) {
            return null;
        }

        return PostSearchType.findPostSearchType(searchBy, searchQuery);
    }

    private Predicate searchByCategory(Long categoryId) {
        return categoryId != null ? QPost.post.category.id.eq(categoryId) : null;
    }

    private OrderSpecifier<?> findOrder(Pageable pageable) {
        Sort.Order order = pageable.getSort().get().findFirst().orElseGet(() -> new Sort.Order(Sort.Direction.DESC, "total"));

        return PostOrderType.findPostOrderQuery(order.getProperty());
    }
}
