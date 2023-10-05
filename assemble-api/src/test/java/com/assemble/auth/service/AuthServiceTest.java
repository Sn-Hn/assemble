package com.assemble.auth.service;

import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.commons.exception.UnauthenticationException;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("AuthService")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Spy
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Test
    void 로그인_성공() {
        // given
        LoginRequest loginRequest = UserFixture.로그인_시도_회원();
        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(UserFixture.회원()));

        // when
        User response = authService.login(loginRequest);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getEmail().getValue()).isEqualTo(loginRequest.getEmail())
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
        assertThatExceptionOfType(UnauthenticationException.class)
                .isThrownBy(() -> authService.login(loginRequest));
    }
}