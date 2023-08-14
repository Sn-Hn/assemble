package com.assemble.user.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class Password {

    @Column(name="PASSWORD")
    private String value;

    public Password(String value, PasswordEncoder passwordEncoder) {
        verifyEmptyPassword(value);
        this.value = passwordEncoder.encode(value);
    }

    protected Password() {
    }

    private void verifyEmptyPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("empty password");
        }
    }

    public boolean isComparePassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(password, this.value)) {
            return true;
        }

        return false;
    }
}
