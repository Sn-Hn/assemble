package com.assemble.post.domain;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Arrays;
import java.util.function.Function;

public enum PostSearchType {
    TITLE("title", (search) -> QPost.post.title.value.like("%" + search + "%")),
    CONTENTS("contents", (search) -> QPost.post.contents.value.like("%" + search + "%")),
    WRITER("writer", (search) -> QPost.post.user.userId.eq(Long.valueOf(search)));

    private String type;

    public Function<String, BooleanExpression> searchQuery;

    PostSearchType(String type, Function<String, BooleanExpression> searchQuery) {
        this.type = type;
        this.searchQuery = searchQuery;
    }

    public static BooleanExpression findPostSearchType(String type, String searchQuery) {
        searchQuery = searchQuery == null ? "" : searchQuery;
        return type != null ? findType(type).getSearchQuery().apply(searchQuery) : null;
    }

    private static PostSearchType findType(String type) {
        return Arrays.stream(values())
                .filter(enumType -> enumType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(PostSearchType.class, type));
    }

    public String getType() {
        return type;
    }

    public Function<String, BooleanExpression> getSearchQuery() {
        return searchQuery;
    }
}
