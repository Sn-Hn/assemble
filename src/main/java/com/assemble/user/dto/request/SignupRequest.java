package com.assemble.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@ApiModel(value = "SignupRequest : 회원가입 요청 값")
@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    @ApiModelProperty(value = "이메일", required = true)
    @NotEmpty
    private String email;
    
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

    @ApiModelProperty(value = "비밀번호", required = true)
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")
    private String password;

    @ApiModelProperty(value = "생년월일", required = true)
    @NotEmpty
    private String birthDate;

    @ApiModelProperty(value = "성별", required = true)
    @NotEmpty
    private String gender;

    private SignupRequest() {
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
