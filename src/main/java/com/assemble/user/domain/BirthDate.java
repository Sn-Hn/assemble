package com.assemble.user.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@Embeddable
@Getter
public class BirthDate {

    @Transient
    private final Pattern pattern = Pattern.compile("/^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$/;");

    @Column(name = "birthDate")
    private String value;

    public BirthDate(String value) {
        verifyBirthDateForm(value);
        this.value = value;
    }

    protected BirthDate() {
    }

    private void verifyBirthDateForm(String birthDate) {
        if (!pattern.matcher(birthDate).matches()) {
            throw new IllegalArgumentException("invalid form birthDate");
        }
    }
}
