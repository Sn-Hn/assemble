package com.assemble.user.fixture;

import com.assemble.user.domain.Email;
import com.assemble.user.domain.Name;
import com.assemble.user.domain.Password;
import com.assemble.user.domain.PhoneNumber;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFixture {
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final String email = "test@test.com";

    private static final String successLoginEmail = "test00@gmail.com";
    private static final String password = "password1!";
    private static final String failedLoginPassword = "password12345!@#$%^%";
    private static final String name = "test";
    private static final String phoneNumber = "01011112222";
    private static final String nickname = "test developer";

    public static SignupRequest 회원가입_정상_신청_회원() {
        return new SignupRequest(email, name, nickname, phoneNumber, password);
    }

    public static LoginRequest 로그인_시도_회원() {
        return new LoginRequest(email, password);
    }

    public static LoginRequest 로그인_실패_회원() {
        return new LoginRequest(email, failedLoginPassword);
    }

    public static LoginRequest 로그인_성공_회원() {
        return new LoginRequest(successLoginEmail, password);
    }

    public static User 회원() {
        return new User(
                new Email(email),
                new Name(name),
                nickname,
                new Password(password, passwordEncoder),
                new PhoneNumber(phoneNumber)
        );
    }
}
