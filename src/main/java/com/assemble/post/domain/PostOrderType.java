package com.assemble.post.domain;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.entity.QPost;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;
import java.util.function.Supplier;

public enum PostOrderType {
    TOTAL("total", () -> QPost.post.postId.desc()),
    LIKE("like", () -> QPost.post.likes.desc()),
    POPULAR("popular", () -> QPost.post.hits.desc());

    private String type;

    private Supplier<OrderSpecifier> orderType;

    PostOrderType(String type, Supplier<OrderSpecifier> orderType) {
        this.type = type;
        this.orderType = orderType;
    }

    public static OrderSpecifier findPostOrderQuery(String type) {
        return type != null ? findType(type).getOrderType().get() : TOTAL.getOrderType().get();
    }

    private static PostOrderType findType(String type) {
        return Arrays.stream(values())
                .filter(enumType -> enumType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(PostOrderType.class, type));
    }

    public String getType() {
        return type;
    }

    public Supplier<OrderSpecifier> getOrderType() {
        return orderType;
    }
}
