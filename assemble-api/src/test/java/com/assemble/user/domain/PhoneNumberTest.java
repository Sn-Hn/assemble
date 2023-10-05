package com.assemble.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PhoneNumber")
class PhoneNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1234a4545", "11#11123", "010-1234-1234"})
    void 유효하지_않은_전화번호_형식_오류(String value) {
        // given
        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new PhoneNumber(value));
    }

    @Test
    void 정상_전화번호() {
        // given
        String value = "01012341234";

        // when
        PhoneNumber phoneNumber = new PhoneNumber(value);

        // then
        assertThat(phoneNumber.getValue()).isEqualTo(value);


    }
}