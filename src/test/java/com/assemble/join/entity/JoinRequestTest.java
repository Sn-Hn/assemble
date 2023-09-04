package com.assemble.join.entity;

import com.assemble.commons.exception.UserBlockException;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.fixture.JoinRequestFixture;
import com.assemble.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("JoinRequestTest")
class JoinRequestTest {

    @Test
    void 가입_신청_시_정상_회원_검증() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();

        // when, then
        assertThatCode(() -> joinRequest.validateAnswerStatusOfJoinRequest())
                .doesNotThrowAnyException();
    }

    @Test
    void 중복_가입_신청_검증() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();

        // when, then
        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequest.validateAlreadyJoinRequest());
    }

    @Test
    void 가입_신청_시_승인된_회원_검증() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.승인된_회원();

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequest.validateAnswerStatusOfJoinRequest());
    }

    @Test
    void 가입_신청_시_차단된_회원_검증() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.차단된_회원();

        // when, then
        assertThatExceptionOfType(UserBlockException.class)
                .isThrownBy(() -> joinRequest.validateAnswerStatusOfJoinRequest());
    }

    @Test
    void 정상_가입_승인() {
        // given
        User user = new User(1L);
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_승인();

        // when
        joinRequest.answerJoinRequest(joinRequestAnswer, user.getUserId());

        // then
        assertAll(
                () -> assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.APPROVAL),
                () -> assertThat(joinRequest.getMeeting().getUser().getUserId()).isEqualTo(user.getUserId())
        );
    }

    /* 처리 = 승인, 거절, 차단 */
    @Test
    void 모임_생성자가_아니면_가입_요청_처리_안됨() {
        // given
        User user = new User(2L);
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_승인();

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequest.answerJoinRequest(joinRequestAnswer, user.getUserId()));
    }

    @Test
    void 정상_가입_신청_취소() {
        // given
        User user = new User(2L);
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원(); /* 가입 신청 UserId=2 */

        // when
        joinRequest.cancelJoinRequest(user.getUserId());

        // then
        assertAll(
                () -> assertThat(joinRequest.getUser().getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.CANCEL)
        );
    }

    @Test
    void 가입_취소_본인_아닌_경우_검증() {
        // given
        User user = new User(1L);
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();

        // when
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequest.cancelJoinRequest(user.getUserId()));
    }

    @Test
    void 가입_취소_시_신청_하지_않은_경우_검증() {
        // given
        User user = new User(2L);
        JoinRequest joinRequest = JoinRequestFixture.거절된_회원(); /* UserId=2 */

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequest.cancelJoinRequest(user.getUserId()));
    }
}