package com.assemble.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
public class PhoneNumber {
    @Transient
    private final Pattern pattern = Pattern.compile("^[0-9]+$");

    @Column(name = "PHONE_NUMBER")
    private String value;

    public PhoneNumber(String value) {
        verifyPhoneNumberFormat(value);
        this.value = value;
    }

    protected PhoneNumber() {
    }

    public String getValue() {
        return value;
    }

    private void verifyPhoneNumberFormat(String phoneNumber) {
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("invalid format phoneNumber");
        }
    }
}
