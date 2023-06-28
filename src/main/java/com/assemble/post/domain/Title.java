package com.assemble.post.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(name = "POST_TITLE")
    private String value;

    public Title(String value) {
        verifyEmpty(value);
        this.value = value;
    }

    protected Title() {}

    public String getValue() {
        return value;
    }

    private void verifyEmpty(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title empty");
        }
    }
}
