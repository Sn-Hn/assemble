package com.assemble.user.entity;

import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "User")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();

        // when
        User user = User.signupUser(signupRequest, passwordEncoder);

        // then
        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getEmail().getValue()).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(user.getName().getValue()).isEqualTo(signupRequest.getName()),
                () -> assertThat(user.getNickName()).isEqualTo(signupRequest.getNickname()),
                () -> assertThat(user.getPhoneNumber().getValue()).isEqualTo(signupRequest.getPhoneNumber()),
                () -> assertThatCode(() -> user.getPassword().isComparePassword(signupRequest.getPassword())).doesNotThrowAnyException()
        );
    }

    @Test
    void 로그인_성공() {
        // given
        LoginRequest loginRequest = UserFixture.로그인_시도_회원();
        User user = UserFixture.회원();

        // when
        // then
        assertThatCode(() -> user.login(loginRequest)).doesNotThrowAnyException();


    }
}