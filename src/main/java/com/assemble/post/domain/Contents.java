package com.assemble.post.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

    @Column(name = "POST_CONTENTS")
    @Lob
    private String value;

    public Contents(String value) {
        verifyEmpty(value);
        this.value = value;
    }

    protected Contents() {
    }

    public String getValue() {
        return value;
    }

    private void verifyEmpty(String contents) {
        if (!StringUtils.hasText(contents)) {
            throw new IllegalArgumentException("contents empty");
        }
    }
}
