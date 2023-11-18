package com.assemble.activity.domain;

public enum ActivityStatus {
    NORMAL("정상"),
    WITHDRAWAL("탈퇴"),
    DISMISS("강제 퇴장");

    private String description;

    ActivityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
