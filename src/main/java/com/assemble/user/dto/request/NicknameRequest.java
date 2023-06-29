package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameRequest {

    @ApiModelProperty(value = "닉네임", required = true)
    private String nickname;

}
