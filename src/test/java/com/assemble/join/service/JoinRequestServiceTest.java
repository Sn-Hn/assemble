package com.assemble.join.service;

import com.assemble.activity.entity.Activity;
import com.assemble.activity.fixture.ActivityFixture;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.UserBlockException;
import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.fixture.PageableFixture;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.entity.JoinRequest;
import com.assemble.join.fixture.JoinRequestFixture;
import com.assemble.join.repository.JoinRequestRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.meeting.repository.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("JoinServiceTest")
@ExtendWith(MockitoExtension.class)
class JoinRequestServiceTest {

    @InjectMocks
    private JoinRequestService joinRequestService;

    @Mock
    private JoinRequestRepository joinRequestRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserContext userContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ActivityRepository activityRepository;

    @Test
    void 모임_가입_신청() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
        JoinRequest joinRequestUser = JoinRequestFixture.정상_신청_회원();
        given(joinRequestRepository.save(any())).willReturn(joinRequestUser);
        given(activityRepository.findByMeetingId(any())).willReturn(List.of());
        given(userContext.getUserId()).willReturn(joinRequestUser.getUser().getUserId());

        // when
        JoinRequest joinRequest = joinRequestService.requestJoinToAssemble(joinRequestDto);

        // then
        assertAll(
                () -> assertThat(joinRequest.getMeeting().getMeetingId()).isEqualTo(joinRequestDto.getMeetingId()),
                () -> assertThat(joinRequest.getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REQUEST)
        );
    }

    @Test
    void 이미_가입_신청한_회원_모임_가입_신청_불가() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        JoinRequest joinRequest = JoinRequestFixture.정상_신청_회원();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(joinRequest));
        given(userContext.getUserId()).willReturn(joinRequest.getUser().getUserId());

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.requestJoinToAssemble(joinRequestDto));
    }

    @Test
    void 이미_가입한_회원_모임_가입_신청_불가() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
        Activity activityUser = ActivityFixture.특정_모임_활동_중인_회원();
        given(activityRepository.findByMeetingId(any())).willReturn(List.of(activityUser));
        given(userContext.getUserId()).willReturn(activityUser.getUser().getUserId());

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.requestJoinToAssemble(joinRequestDto));
    }

    @Test
    void 이미_승인된_회원_모임_가입_신청_불가() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        JoinRequest joinRequest = JoinRequestFixture.승인된_회원();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(joinRequest));
        given(userContext.getUserId()).willReturn(joinRequest.getUser().getUserId());

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.requestJoinToAssemble(joinRequestDto));
    }

    @Test
    void 차단된_회원_모임_가입_신청_불가() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        JoinRequest joinRequest = JoinRequestFixture.차단된_회원();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(joinRequest));
        given(userContext.getUserId()).willReturn(joinRequest.getUser().getUserId());

        // when, then
        assertThatExceptionOfType(UserBlockException.class)
                .isThrownBy(() -> joinRequestService.requestJoinToAssemble(joinRequestDto));
    }

    @Test
    void 모임_가입_승인_검증() {
        // given
        String status = "APPROVAL";
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        JoinRequest joinRequest = joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer);

        // then
        assertAll(
                () -> assertThat(joinRequest.getMeeting().getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus().toString()).isEqualTo(status)
        );

        verify(eventPublisher, times(1)).publishEvent(any(JoinRequestEvent.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"REJECT", "BLOCK"})
    void 모임_가입_처리_검증(String status) {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        JoinRequest joinRequest = joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer);

        // then
        assertAll(
                () -> assertThat(joinRequest.getMeeting().getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus().toString()).isEqualTo(status)
        );

        verify(eventPublisher, times(0)).publishEvent(any(JoinRequestEvent.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"APPROVAL", "REJECT", "BLOCK"})
    void 모임_생성자만_모임_가입_처리_가능(String status) {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        // 모임 생성자 UserId=1
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        // 로그인한 회원 UserId=2
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer));

    }

    @ParameterizedTest
    @ValueSource(strings = {"APPROVAL", "REJECT"})
    void 이미_처리된_회원은_처리_불가(String status) {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_승인();
        // 모임 생성자 UserId=1
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.가입_처리_응답(JoinRequestStatus.valueOf(status))));
        // 로그인한 회원 UserId=1
        given(userContext.getUserId()).willReturn(1L);

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer));

    }

    @Test
    void 차단된_회원은_차단해제만_가능() {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_거절();
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.차단된_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        JoinRequest joinRequest = joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer);

        // then
        assertAll(
                () -> assertThat(joinRequest.getMeeting().getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REJECT)
        );

        verify(eventPublisher, times(0)).publishEvent(any(JoinRequestEvent.class));
    }

    @Test
    void 차단해제_성공() {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_거절();
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.차단된_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        JoinRequest joinRequest = joinRequestService.processJoinRequestFromAssemble(joinRequestAnswer);

        // then
        assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REJECT);
    }

    @Test
    void 정상_모임_가입_취소() {
        // given
        Meeting meeting = MeetingFixture.모임();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(2L);

        // when
        boolean isCancel = joinRequestService.cancelJoinOfAssemble(meeting.getMeetingId());

        // then
        assertThat(isCancel).isTrue();
    }

    @Test
    void 가입_취소_본인_아닌_경우_실패() {
        // given
        Meeting meeting = MeetingFixture.모임();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.cancelJoinOfAssemble(meeting.getMeetingId()));
    }

    @Test
    void 가입_취소_시_신청_하지_않은_경우_검증() {
        // given
        Meeting meeting = MeetingFixture.모임();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.거절된_회원()));
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.cancelJoinOfAssemble(meeting.getMeetingId()));
    }

    @Test
    void 모임_가입_신청_조회() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Meeting meeting = MeetingFixture.모임();
        Long meetingId = meeting.getMeetingId();
        given(meetingRepository.findById(anyLong())).willReturn(Optional.of(meeting));
        given(joinRequestRepository.findAllByMeetingId(anyLong())).willReturn(List.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsToMeeting(meetingId);

        // then
        assertAll(
                () -> assertThat(joinRequests).isNotEmpty(),
                () -> assertThat(joinRequests.size()).isEqualTo(1),
                () -> assertThat(joinRequests.stream().findFirst().get().getMeeting().getMeetingId()).isEqualTo(meetingId),
                () -> assertThat(joinRequests.stream().findFirst().get().getMeeting().getUser().getUserId()).isEqualTo(meeting.getUser().getUserId())
        );
    }

    @Test
    void 모임장_아니면_가입_신청_조회_불가능() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Meeting meeting = MeetingFixture.모임();
        Long meetingId = meeting.getMeetingId();
        given(meetingRepository.findById(anyLong())).willReturn(Optional.of(meeting));
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.getJoinRequestsToMeeting(meetingId));

    }
}