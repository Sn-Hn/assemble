package com.assemble.user.service;

import com.assemble.commons.base.UserContext;
import com.assemble.file.service.FileService;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationService verificationService;

    @Mock
    private FileService fileService;

    @Spy
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Mock
    private UserContext userContext;

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        MultipartFile profileImage = null;
        given(userRepository.save(any()))
                .willReturn(UserFixture.회원());

        // when
        User signupUser = userService.signup(signupRequest);

        // then
        assertAll(
                () -> assertThat(signupUser).isNotNull(),
                () -> assertThat(signupUser.getEmail().getValue()).isEqualTo(signupRequest.getEmail()),
                () -> assertThat(signupUser.getName().getValue()).isEqualTo(signupRequest.getName())
        );
    }

    @Test
    void 특정_회원_조회() {
        // given
        Long userId = 1L;
        given(userRepository.findById(any())).willReturn(Optional.of(UserFixture.회원()));

        // when
        User userInfo = userService.findUserInfo(userId);

        // then
        assertAll(
                () -> assertThat(userInfo).isNotNull(),
                () -> assertThat(userInfo.getUserId()).isEqualTo(userId)
        );
    }

    @Test
    void 회원_탈퇴() {
        // given
        given(userContext.getUserId()).willReturn(1L);

        // when
        boolean isWithdrawal = userService.withdrawUser();

        // then
        assertThat(isWithdrawal).isTrue();
    }

    @Test
    void 회원정보_수정() {
        // given
        ModifiedUserRequest modifiedUserRequest = UserFixture.회원정보_수정();
        MultipartFile profileImage = null;
        given(userRepository.findById(any()))
                .willReturn(Optional.of(UserFixture.회원()));

        // when
        User signupUser = userService.modifyUserInfo(modifiedUserRequest);

        // then
        assertAll(
                () -> assertThat(signupUser).isNotNull(),
                () -> assertThat(signupUser.getName().getValue()).isEqualTo(modifiedUserRequest.getName()),
                () -> assertThat(signupUser.getNickname()).isEqualTo(modifiedUserRequest.getNickname())
        );
    }
}