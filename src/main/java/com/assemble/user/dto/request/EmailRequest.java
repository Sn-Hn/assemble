package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@ToString
public class EmailRequest {

    @ApiModelProperty(value = "이메일", required = true)
    @NotEmpty
    private String email;

}
