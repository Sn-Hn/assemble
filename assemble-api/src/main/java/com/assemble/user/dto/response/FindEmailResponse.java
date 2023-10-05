package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindEmailResponse {

    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "회원 이름")
    private String name;

    @ApiModelProperty(value = "회원 이메일")
    private String email;

    public FindEmailResponse(User user) {
        this (user.getUserId(), user.getName().getValue(), user.getEmail().getValue());
    }
}
