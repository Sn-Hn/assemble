package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailRequest {

    @ApiModelProperty(value = "이메일", required = true)
    private String email;

}
