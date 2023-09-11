package com.assemble.user.fixture;

import com.assemble.user.domain.*;
import com.assemble.user.dto.request.*;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.entity.User;
import com.assemble.user.entity.UserImage;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserFixture {
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final Long userId = 1L;
    private static final Long withdrawUserId = 3L;
    private static final String email = "test1@test.com";
    private static final String notDuplicationEmail = "test10012@test.com";
    private static final String secondEmail = "test2@test.com";
    private static final String successLoginEmail = "test00@gmail.com";
    private static final String password = "password1!";
    private static final String changePassword = "password2!";
    private static final String failedLoginPassword = "passd12!@#$";
    private static final String name = "test";
    private static final String phoneNumber = "01011112222";
    private static final String nickname = "test developer";
    private static final String notDuplicationNickname = "닉네임";
    private static final String secondNickname = "test developer2";
    private static final String birthDate = "20000101";
    private static final String duplicationNickname = "test01";
    private static final String modifiedName = "수정된 이름";
    private static final String modifiedNickname = "수정된 닉네임";
    private static final String modifiedPhoneNumber = "01012341234";
    private static final String modifiedBirthDate = "20000101";
    private static final String man = "MAN";

    public static SignupRequest 회원가입_정상_신청_회원() {
        return new SignupRequest(email, name, nickname, phoneNumber, password, birthDate, man);
    }

    public static SignupRequest 회원가입_정상_신청_두번째_회원() {
        return new SignupRequest(secondEmail, name, secondNickname, phoneNumber, password, birthDate, man);
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
                new Password(passwordEncoder.encode(password)),
                new PhoneNumber(phoneNumber),
                new BirthDate(birthDate),
                UserRole.USER,
                UserStatus.NORMAL,
                new UserImages(),
                LocalDateTime.now(),
                Gender.MAN
        );
    }

    public static User 관리자() {
        return new User(
                2L,
                new Email("test01@gmail.com"),
                new Name("관리자"),
                "관리자",
                new Password(passwordEncoder.encode(password)),
                new PhoneNumber(phoneNumber),
                new BirthDate(birthDate),
                UserRole.ADMIN,
                UserStatus.NORMAL,
                new UserImages(),
                LocalDateTime.now(),
                Gender.MAN
        );
    }

    public static User 탈퇴할_회원() {
        return new User(
                withdrawUserId,
                new Email(email),
                new Name(name),
                nickname,
                new Password(passwordEncoder.encode(password)),
                new PhoneNumber(phoneNumber),
                new BirthDate(birthDate),
                UserRole.USER,
                UserStatus.NORMAL,
                new UserImages(),
                LocalDateTime.now(),
                Gender.MAN
        );
    }

    public static EmailRequest 중복_아닌_이메일() {
        return new EmailRequest(notDuplicationEmail);
    }

    public static EmailRequest 중복_이메일() {
        return new EmailRequest(successLoginEmail);
    }

    public static NicknameRequest 중복_아닌_닉네임() {
        return new NicknameRequest(notDuplicationNickname);
    }

    public static NicknameRequest 중복_닉네임() {
        return new NicknameRequest(duplicationNickname);
    }

    public static ModifiedUserRequest 회원정보_수정() {
        return new ModifiedUserRequest(modifiedName, modifiedNickname, modifiedPhoneNumber, modifiedBirthDate);
    }

    public static FindEmailRequest 이메일_찾기_요청() {
        return new FindEmailRequest(name, phoneNumber, birthDate);
    }

    public static ChangePasswordRequest 비밀번호_변경_요청() {
        return new ChangePasswordRequest(email, changePassword);
    }

    public static ValidationUserRequest 정상_본인_확인_요청() {
        return new ValidationUserRequest(email, name, phoneNumber, birthDate);
    }

    public static ValidationUserRequest 비정상_본인_확인_요청() {
        return new ValidationUserRequest(email, "wrongName", phoneNumber, birthDate);
    }
}
