package com.assemble.user.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class PhoneNumber {
    @Transient
    private final Pattern pattern = Pattern.compile("^[0-9]{9,11}$");

    @Column(name = "PHONE_NUMBER")
    private String value;

    public PhoneNumber(String value) {
        validatePhoneNumberForm(value);
        validateEmptyPhoneNumber(value);
        this.value = value;
    }

    protected PhoneNumber() {
    }

    public String getValue() {
        return value;
    }

    private void validateEmptyPhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new IllegalArgumentException("empty phoneNumber");
        }
    }

    private void validatePhoneNumberForm(String phoneNumber) {
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("invalid form phoneNumber");
        }
    }
}
