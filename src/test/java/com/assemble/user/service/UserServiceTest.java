package com.assemble.user.service;

import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void 로그인_성공() {
        // given
        LoginRequest loginRequest = UserFixture.로그인_시도_회원();
        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(UserFixture.회원()));

        // when
        User user = userService.login(loginRequest);

        // then
        assertAll(
                () -> assertThat(user).isNotNull()
        );
    }

    @Test
    void 잘못된_비밀번호_로그인_실패() {
        // given
        LoginRequest loginRequest = UserFixture.로그인_실패_회원();
        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(UserFixture.회원()));

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.login(loginRequest));
    }

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        given(userRepository.save(any()))
                .willReturn(UserFixture.회원());

        // when
        User signupUser = userService.signup(signupRequest);

        // then
        assertAll(
                () -> assertThat(signupUser).isNotNull(),
                () -> assertThat(signupUser.getEmail().getValue()).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(signupUser.getName().getValue()).isEqualTo(signupRequest.getName())
        );
    }
}