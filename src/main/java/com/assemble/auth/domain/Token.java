package com.assemble.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class Token {

    private String accessToken;

    private String refreshToken;
}
