package com.assemble.user.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name="USER_NAME")
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
