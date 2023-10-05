package com.assemble.meeting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Contents")
class DescriptionTest {

    @Test
    void 내용_빈값_검증() {
        // given
        String empty = "";

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Description(empty));

    }

    @Test
    void 내용_NULL_검증() {
        // given, when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Description(null));
    }

    @Test
    void 내용_정상_검증() {
        // given
        String contentString = "내용입니다~";

        // when
        Description description = new Description(contentString);

        // then
        assertThat(description.getValue()).isEqualTo(contentString);

    }
}