package com.assemble.auth.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

    @ApiModelProperty(value = "Access Token")
    private String accessToken;
}
