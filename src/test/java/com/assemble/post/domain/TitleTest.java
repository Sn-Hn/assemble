package com.assemble.post.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Title")
class TitleTest {

    @Test
    void 제목_빈값_검증() {
        // given
        String empty = "";

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Title(empty));

    }

    @Test
    void 제목_NULL_검증() {
        // given, when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Title(null));
    }

    @Test
    void 제목_정상_검증() {
        // given
        String titleString = "title";

        // when
        Title title = new Title(titleString);

        // then
        assertThat(title.getValue()).isEqualTo(titleString);

    }
}