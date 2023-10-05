package com.assemble.meeting.domain;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.meeting.entity.QMeeting;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Arrays;
import java.util.function.Function;

public enum MeetingSearchType {
    NAME("name", (search) -> QMeeting.meeting.name.value.like("%" + search + "%")),
    DESCRIPTION("description", (search) -> QMeeting.meeting.description.value.like("%" + search + "%")),
    WRITER("writer", (search) -> QMeeting.meeting.user.nickname.like("%" + search + "%"));

    private String type;

    public Function<String, BooleanExpression> searchQuery;

    MeetingSearchType(String type, Function<String, BooleanExpression> searchQuery) {
        this.type = type;
        this.searchQuery = searchQuery;
    }

    public static BooleanExpression findPostSearchType(String type, String searchQuery) {
        return findType(type).getSearchQuery().apply(searchQuery);
    }

    private static MeetingSearchType findType(String type) {
        return Arrays.stream(values())
                .filter(enumType -> enumType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MeetingSearchType.class, type));
    }

    public String getType() {
        return type;
    }

    public Function<String, BooleanExpression> getSearchQuery() {
        return searchQuery;
    }
}
