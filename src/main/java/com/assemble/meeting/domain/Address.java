package com.assemble.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
public class Address {

    private String zipCode;

    @Column(name = "ROAD_NAME_ADDRESS")
    private String roadName;

    @Column(name = "LOT_NUMBER_ADDRESS")
    private String lotNumber;

    @Column(name = "DETAIL_ADDRESS")
    private String detail;

    protected Address() {
    }
}
