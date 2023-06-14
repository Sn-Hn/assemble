package com.assemble.user.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;

@Embeddable
public class Name {

    private String value;

    public Name(String value) {
        verifyEmpty(value);
        this.value = value;
    }

    protected Name() {
    }

    public String getValue() {
        return value;
    }

    private void verifyEmpty(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name empty");
        }
    }
}
