package com.assemble.user.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class Password {
    @Transient
    private final Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");

    @Transient
    private PasswordEncoder passwordEncoder;

    private String value;

    public Password(String value, PasswordEncoder passwordEncoder) {
        verifyPasswordFormat(value);
        this.value = passwordEncoder.encode(value);
        this.passwordEncoder = passwordEncoder;
    }

    public Password() {
    }

    private void verifyPasswordFormat(String password) {
        if (!pattern.matcher(password).matches()) {
            throw new IllegalArgumentException("invalid format password");
        }
    }

    public void isComparePassword(String password) {
        if (!passwordEncoder.matches(password, this.value)) {
            throw new IllegalArgumentException("invalid password");
        }
    }
}
