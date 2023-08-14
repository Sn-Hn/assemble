package com.assemble.auth.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@ApiModel(value = "LoginRequest : 회원가입 요청 값")
@Getter
@AllArgsConstructor
public class LoginRequest {

    @ApiModelProperty(value = "이메일", required = true)
    @NotEmpty
    private String email;

    @ApiModelProperty(value = "비밀번호", required = true)
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")
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
