package com.assemble.user.fixture;

import com.assemble.user.domain.*;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

public class UserFixture {
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final Long userId = 1L;
    private static final String email = "test@test.com";
    private static final String secondEmail = "test2@test.com";
    private static final String successLoginEmail = "test00@gmail.com";
    private static final String password = "password1!";
    private static final String failedLoginPassword = "password12345!@#$%^%";
    private static final String name = "test";
    private static final String phoneNumber = "01011112222";
    private static final String nickname = "test developer";
    private static final String secondNickname = "test developer2";

    private static final String duplicationNickname = "test01";

    public static SignupRequest 회원가입_정상_신청_회원() {
        return new SignupRequest(email, name, nickname, phoneNumber, password);
    }

    public static SignupRequest 회원가입_정상_신청_두번째_회원() {
        return new SignupRequest(secondEmail, name, secondNickname, phoneNumber, password);
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
                userId,
                new Email(email),
                new Name(name),
                nickname,
                new Password(password, passwordEncoder),
                new PhoneNumber(phoneNumber),
                UserRole.USER,
                new ArrayList<>()
        );
    }

    public static EmailRequest 중복_아닌_이메일() {
        return new EmailRequest(email);
    }

    public static EmailRequest 중복_이메일() {
        return new EmailRequest(successLoginEmail);
    }

    public static NicknameRequest 중복_아닌_닉네임() {
        return new NicknameRequest(nickname);
    }

    public static NicknameRequest 중복_닉네임() {
        return new NicknameRequest(duplicationNickname);
    }
}
