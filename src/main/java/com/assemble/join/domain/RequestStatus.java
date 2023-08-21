package com.assemble.join.domain;

public enum RequestStatus {
    REQUEST("신청"),
    REJECT("거절"),
    APPROVAL("승인"),
    BLOCK("차단");

    private String description;

    RequestStatus(String description) {
        this.description = description;
    }
}
