package com.assemble.post.domain;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Arrays;
import java.util.function.Function;

public enum PostSearchType {
    TITLE("title", (search) -> QPost.post.title.value.like("%" + search + "%")),
    CONTENTS("contents", (search) -> QPost.post.contents.value.like("%" + search + "%"));

    private String code;

    public Function<String, BooleanExpression> findSearchType;

    PostSearchType(String code, Function<String, BooleanExpression> findSearchType) {
        this.code = code;
        this.findSearchType = findSearchType;
    }

    public static BooleanExpression findPostSearchType(String type, String searchQuery) {
        return findType(type).getFindSearchType().apply(searchQuery);
    }

    private static PostSearchType findType(String type) {
        return Arrays.stream(values())
                .filter(enumType -> enumType.getCode().equals(type))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(PostSearchType.class, type));
    }

    public String getCode() {
        return code;
    }

    public Function<String, BooleanExpression> getFindSearchType() {
        return findSearchType;
    }
}
