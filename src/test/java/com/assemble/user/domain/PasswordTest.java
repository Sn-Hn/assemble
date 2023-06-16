package com.assemble.user.domain;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Password")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PasswordTest {

    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdefg123", "a1#", "test", ""})
    void 유효하지_않은_비밀번호_형식_오류(String value) {
        // given
        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Password(value, passwordEncoder));
    }

    @Test
    void 정상_비밀번호() {
        // given
        String value = "password1!";

        // when
        Password password = new Password(value, passwordEncoder);

        assertAll(
                () -> assertThat(password).isNotNull(),
                () -> assertThatCode(() -> password.isComparePassword(value, passwordEncoder)).doesNotThrowAnyException()
        );
    }
}