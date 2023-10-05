package com.assemble.user.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordChangeToken {

    @ApiModelProperty(value = "비밀번호 변경 토큰")
    private String token;
    
}
