package com.assemble.meeting.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Description {

    @Column(name = "MEETING_DESCRIPTION")
    @Lob
    private String value;

    public Description(String value) {
        validateEmpty(value);
        this.value = value;
    }

    protected Description() {
    }

    public String getValue() {
        return value;
    }

    private void validateEmpty(String contents) {
        if (!StringUtils.hasText(contents)) {
            throw new IllegalArgumentException("contents empty");
        }
    }
}
