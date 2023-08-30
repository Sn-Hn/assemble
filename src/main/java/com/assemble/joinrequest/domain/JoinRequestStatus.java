package com.assemble.joinrequest.domain;

public enum JoinRequestStatus {
    REQUEST("신청"),
    REJECT("거절"),
    APPROVAL("승인"),
    BLOCK("차단"),
    CANCEL("신청 취소");

    private String description;

    JoinRequestStatus(String description) {
        this.description = description;
    }
}
