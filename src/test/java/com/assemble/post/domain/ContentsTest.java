package com.assemble.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Contents")
class ContentsTest {

    @Test
    void 내용_빈값_검증() {
        // given
        String empty = "";

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Contents(empty));

    }

    @Test
    void 내용_NULL_검증() {
        // given, when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Contents(null));
    }

    @Test
    void 내용_정상_검증() {
        // given
        String contentString = "내용입니다~";

        // when
        Contents contents = new Contents(contentString);

        // then
        assertThat(contents.getValue()).isEqualTo(contentString);

    }
}