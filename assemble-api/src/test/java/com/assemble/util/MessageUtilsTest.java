package com.assemble.util;

import com.assemble.annotation.TestCustomProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestCustomProperty
@DisplayName("MessageUtils")
class MessageUtilsTest {

    @Test
    void 메시지_변환_검증() {
        // given
        String messageKey = "error.notfound";

        // when
        String message = MessageUtils.getMessage(messageKey);

        // then
        Assertions.assertThat(message).isEqualTo("NotFound");
    }
}