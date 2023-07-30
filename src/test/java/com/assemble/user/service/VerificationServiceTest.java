package com.assemble.user.service;

import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerificationService")
public class VerificationServiceTest {

    @InjectMocks
    private VerificationService verificationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 이메일_중복_아님() {
        // given
        User user = UserFixture.회원();
        String email = user.getEmail().getValue();
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));

        // when
        boolean isDuplicationEmail = verificationService.isDuplicationEmail(email);

        // then
        assertThat(isDuplicationEmail).isTrue();
    }

    @Test
    void 이메일_중복() {
        // given
        User user = UserFixture.회원();
        String email = user.getEmail().getValue();
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

        // when
        boolean isDuplicationEmail = verificationService.isDuplicationEmail(email);

        // then
        assertThat(isDuplicationEmail).isFalse();
    }

    @Test
    void 닉네임_중복_아님() {
        // given
        User user = UserFixture.회원();
        String nickname = user.getNickname();
        given(userRepository.findByNickname(any())).willReturn(Optional.of(user));

        // when
        boolean isDuplicationNickname = verificationService.isDuplicationNickname(nickname);

        // then
        assertThat(isDuplicationNickname).isTrue();
    }

    @Test
    void 닉네임_중복() {
        // given
        User user = UserFixture.회원();
        String nickname = user.getNickname();
        given(userRepository.findByNickname(any())).willReturn(Optional.empty());

        // when
        boolean isDuplicationNickname = verificationService.isDuplicationNickname(nickname);

        // then
        assertThat(isDuplicationNickname).isFalse();
    }
}
