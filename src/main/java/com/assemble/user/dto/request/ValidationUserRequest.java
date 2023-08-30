package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class ValidationUserRequest {

    @ApiModelProperty(value = "이메일", required = true)
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @ApiModelProperty(value = "이름", required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "핸드폰 번호", required = true)
    @NotEmpty
    @Pattern(regexp = "^[0-9]{9,11}+$")
    private String phoneNumber;

    @Override
    public String toString() {
        return "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'';
    }
}
