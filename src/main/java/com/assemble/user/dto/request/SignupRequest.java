package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(value = "SignupRequest : 회원가입 요청 값")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class SignupRequest {
    @ApiModelProperty(value = "이메일", required = true)
    private String email;
    
    @ApiModelProperty(value = "이름", required = true)
    private String name;

    @ApiModelProperty(value = "닉네임", required = true)
    private String nickname;

    @ApiModelProperty(value = "핸드폰 번호", required = true)
    private String phoneNumber;

    @ApiModelProperty(value = "비밀번호", required = true)
    private String password;

    public SignupRequest() {
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password +
                '}';
    }
}
