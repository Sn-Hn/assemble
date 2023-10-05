package com.assemble.meeting.domain;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.meeting.entity.QMeeting;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;
import java.util.function.Supplier;

public enum MeetingOrderByType {
    TOTAL("total", () -> QMeeting.meeting.meetingId.desc()),
    LIKE("like", () -> QMeeting.meeting.likes.desc()),
    POPULAR("popular", () -> QMeeting.meeting.hits.desc());

    private String type;

    private Supplier<OrderSpecifier> orderType;

    MeetingOrderByType(String type, Supplier<OrderSpecifier> orderType) {
        this.type = type;
        this.orderType = orderType;
    }

    public static OrderSpecifier findPostOrderQuery(String type) {
        return type != null ? findType(type).getOrderType().get() : TOTAL.getOrderType().get();
    }

    private static MeetingOrderByType findType(String type) {
        return Arrays.stream(values())
                .filter(enumType -> enumType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MeetingOrderByType.class, type));
    }

    public String getType() {
        return type;
    }

    public Supplier<OrderSpecifier> getOrderType() {
        return orderType;
    }
}
