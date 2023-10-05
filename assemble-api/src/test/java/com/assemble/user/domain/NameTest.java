package com.assemble.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Name")
class NameTest {

    @Test
    void 공백_이름_오류() {
        // given
        String value = "";

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Name(value));
    }

    @Test
    void 정상_이름() {
        // given
        String value = "test";

        // when
        Name name = new Name(value);

        // given
        assertThat(name.getValue()).isEqualTo(value);
    }
}