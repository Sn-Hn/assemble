package com.assemble.meeting.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MeetingName {

    @Column(name = "MEETING_NAME")
    private String value;

    public MeetingName(String value) {
        validateEmpty(value);
        this.value = value;
    }

    protected MeetingName() {}

    public String getValue() {
        return value;
    }

    private void validateEmpty(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name empty");
        }
    }
}
