package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NicknameRequest {

    @ApiModelProperty(value = "닉네임", required = true)
    private String nickname;

}
