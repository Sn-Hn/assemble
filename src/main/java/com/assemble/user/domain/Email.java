package com.assemble.user.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    @Transient
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

    @Column(name="EMAIL")
    private String value;

    protected Email() {}

    public Email(String value) {
        verifyEmptyEmail(value);
        verifyEmailFormat(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private void verifyEmptyEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("empty email");
        }
    }

    private void verifyEmailFormat(String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("invalid format email");
        }
    }
}
