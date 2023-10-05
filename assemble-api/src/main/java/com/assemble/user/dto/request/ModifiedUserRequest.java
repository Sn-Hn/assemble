package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class ModifiedUserRequest {

    @ApiModelProperty(value = "이름", required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "닉네임", required = true)
    @NotEmpty
    private String nickname;

    @ApiModelProperty(value = "핸드폰 번호", required = true)
    @NotEmpty
    @Pattern(regexp = "^[0-9]{9,11}+$")
    private String phoneNumber;

    @ApiModelProperty(value = "생년월일", required = true)
    @NotEmpty
    private String birthDate;

    private ModifiedUserRequest() {
    }
}
