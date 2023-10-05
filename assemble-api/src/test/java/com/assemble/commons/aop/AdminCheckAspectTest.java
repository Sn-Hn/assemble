package com.assemble.commons.aop;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotAdminException;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("관리자 검증 AOP Test")
@ExtendWith(MockitoExtension.class)
class AdminCheckAspectTest {

    @InjectMocks
    private AdminCheckAspect adminCheckAspect;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 정상_관리자_검증() {
        // given
        User admin = UserFixture.관리자();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(admin));
        given(userContext.getUserId()).willReturn(admin.getUserId());

        // when, then
        assertThatCode(() -> adminCheckAspect.doAdminCheck()).doesNotThrowAnyException();
    }

    @Test
    void 관리자_아닌_경우_검증() {
        // given
        User user = UserFixture.회원();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userContext.getUserId()).willReturn(user.getUserId());

        // when, then
        assertThatExceptionOfType(NotAdminException.class)
                .isThrownBy(() -> adminCheckAspect.doAdminCheck());
    }
}