package com.assemble.auth.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ApiModel(value = "LoginRequest : 회원가입 요청 값")
@Getter
@AllArgsConstructor
public class LoginRequest {

    @ApiModelProperty(value = "이메일", required = true)
    private String email;

    @ApiModelProperty(value = "비밀번호", required = true)
    private String password;

    private LoginRequest() {

    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
