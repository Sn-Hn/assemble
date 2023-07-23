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
    public Page<Post> findAllBySearch(PostSearchRequest postSearchRequest, Pageable pageable) {
        List<Post> posts = queryFactory.select(QPost.post, QLikes.likes)
                .from(QPost.post)
                .leftJoin(QLikes.likes)
                .on(eqUserId(), eqPostId())
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

        return new PageImpl<>(posts, pageable, posts.size());
    }

    private static BooleanExpression eqPostId() {
        return QLikes.likes.post.postId.eq(QPost.post.postId);
    }

    private static BooleanExpression eqUserId() {
        return QLikes.likes.user.userId.eq(BaseRequest.getUserId());
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
