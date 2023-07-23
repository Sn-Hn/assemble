package com.assemble.auth.domain;

import com.assemble.annotation.TestCustomProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestCustomProperty
@DisplayName("JwtProvider")
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void Access_Token_생성() {
        // given
        Long userId = 1L;
        String email = "test00@gmail.com";

        // when
        String accessToken = jwtProvider.createAccessToken(userId, email);

        // then
        assertAll(
                () -> assertThat(accessToken).isNotEmpty(),
                () -> assertThat(jwtProvider.isValidToken(accessToken)).isTrue()
        );
    }

    @Test
    void 유효하지_않은_Access_Token_검증() {
        // given
        String accessToken = "test";

        // when
        boolean isValid = jwtProvider.isValidToken(accessToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void Token_Subject_검증() {
        // given
        Long id = 1L;
        String email = "test00@gmail.com";
        String accessToken = jwtProvider.createAccessToken(id, email);

        String userId = jwtProvider.getUserId(accessToken);

        assertThat(userId).isEqualTo(id.toString());
    }

    @Test
    void Token_Email_검증() {
        // given
        Long id = 1L;
        String email = "test00@gmail.com";
        String accessToken = jwtProvider.createAccessToken(id, email);

        String userEmail = jwtProvider.getEmail(accessToken);

        assertThat(userEmail).isEqualTo(email);
    }
}