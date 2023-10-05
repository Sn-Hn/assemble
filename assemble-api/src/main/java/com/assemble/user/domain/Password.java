package com.assemble.user.domain;

import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Password {

    @Column(name="PASSWORD")
    private String value;

    public Password(String value) {
        validateEmptyPassword(value);
        this.value = value;
    }

    protected Password() {
    }

    private void validateEmptyPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("empty password");
        }
    }
}
