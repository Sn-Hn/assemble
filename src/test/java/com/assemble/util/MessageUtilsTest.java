package com.assemble.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
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