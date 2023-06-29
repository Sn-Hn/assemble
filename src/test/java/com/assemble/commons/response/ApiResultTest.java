package com.assemble.commons.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResult")
class ApiResultTest {

    @Test
    void 성공_응답_검증() {
        // given
        // when
        ApiResult ok = ApiResult.ok(true);

        // then
        assertAll(
                () -> assertThat(ok.isSuccess()).isTrue(),
                () -> assertThat(ok.getResponse()).isEqualTo(true),
                () -> assertThat(ok.getError()).isNull()
        );
    }

    @Test
    void 오류_응답_검증() {
        // given
        String errorMessage = "오류 발생";

        // when
        ApiResult<?> error = ApiResult.error(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

        // then
        assertAll(
                () -> assertThat(error.isSuccess()).isFalse(),
                () -> assertThat(error.getError().getMessage()).isEqualTo(errorMessage),
                () -> assertThat(error.getError().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                () -> assertThat(error.getResponse()).isNull()
        );
    }
}