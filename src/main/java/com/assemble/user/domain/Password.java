package com.assemble.user.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class Password {
    @Transient
    private final Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");

    @Column(name="PASSWORD")
    private String value;

    public Password(String value, PasswordEncoder passwordEncoder) {
        verifyEmptyPassword(value);
        verifyPasswordForm(value);
        this.value = passwordEncoder.encode(value);
    }

    protected Password() {
    }

    private void verifyEmptyPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("empty password");
        }
    }

    private void verifyPasswordForm(String password) {
        if (!pattern.matcher(password).matches()) {
            throw new IllegalArgumentException("invalid form password");
        }
    }

    public boolean isComparePassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(password, this.value)) {
            return true;
        }

        return false;
    }
}
