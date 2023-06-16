package com.assemble.user.domain;

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
        verifyEmailFormat(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private void verifyEmailFormat(String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("invalid format email");
        }
    }
}
