package com.assemble.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email")
class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"test", "test@test", ""})
    void 유효하지_않은_이메일_형식_오류(String value) {
        // given
        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email(value));
    }

    @Test
    void 정상_이메일_형식() {
        // given
        String value = "test@test.com";

        // when
        Email email = new Email(value);

        //then
        assertThat(email.getValue()).isEqualTo(value);
    }
}