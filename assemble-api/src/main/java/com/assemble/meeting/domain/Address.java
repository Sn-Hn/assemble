package com.assemble.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
public class Address {

    @Column(name = "ADDRESS")
    private String value;

    protected Address() {
    }
}
