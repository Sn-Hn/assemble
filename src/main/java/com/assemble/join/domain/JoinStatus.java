package com.assemble.join.domain;

public enum JoinStatus {
    NORMAL("정상"),
    WITHDRAWAL("탈퇴");

    private String description;

    JoinStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
