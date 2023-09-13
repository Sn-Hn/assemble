package com.assemble.activity.service;

import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.fixture.ActivityFixture;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.fixture.PageableFixture;
import com.assemble.meeting.entity.Meeting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("Activity Service Test")
@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 회원이_활동_중인_모임_목록_조회() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Activity activity = ActivityFixture.특정_모임_활동_중인_회원();
        given(userContext.getUserId()).willReturn(1L);
        given(activityRepository.countByActiveAssembles(anyLong())).willReturn(1L);
        given(activityRepository.findAllByActiveAssembles(anyLong(), any())).willReturn(List.of(activity.getMeeting()));

        // when
        Page<Meeting> activeAssembles = activityService.getActiveAssembles(pageable);

        // then
        assertAll(
                () -> assertThat(activeAssembles).isNotEmpty(),
                () -> assertThat(activeAssembles.getTotalElements()).isEqualTo(1),
                () -> assertThat(activeAssembles.get().findAny().get().getUser().getUserId()).isEqualTo(userContext.getUserId())
        );
    }

    @Test
    void 모임에_활동_중인_회원_목록_조회() {
        // given
        Activity activity = ActivityFixture.특정_모임_활동_중인_회원();
        Long meetingId = activity.getMeeting().getMeetingId();
        given(activityRepository.findAllByUserOfAssemble(anyLong())).willReturn(List.of(activity));

        // when
        List<Activity> activities = activityService.getJoinUserOfAssemble(meetingId);

        // then
        assertAll(
                () -> assertThat(activities).isNotEmpty(),
                () -> assertThat(activities.stream().findAny().get().getMeeting().getMeetingId()).isEqualTo(meetingId)
        );
    }
    
    @Test
    void 정상_모임_탈퇴() {
        // given
        given(userContext.getUserId()).willReturn(2L);
        Activity activity = ActivityFixture.특정_모임_활동_중인_회원();
        Long meetingId = activity.getMeeting().getMeetingId();
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(activity));

        // when
        boolean isWithdrawal = activityService.withdrawJoinAssemble(meetingId);

        // then
        assertAll(
                () -> assertThat(isWithdrawal).isTrue(),
                () -> assertThat(activity.getStatus()).isEqualTo(ActivityStatus.WITHDRAWAL)
        );
    }

    @Test
    void 모임장은_탈퇴_불가능() {
        // given
        given(userContext.getUserId()).willReturn(1L);
        Activity activity = ActivityFixture.특정_모임_활동_중인_회원();
        Long meetingId = activity.getMeeting().getMeetingId();
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(activity));

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> activityService.withdrawJoinAssemble(meetingId));

    }

    @Test
    void 이미_탈퇴한_모임은_탈퇴_불가능() {
        // given
        Long meetingId = 1L;
        given(userContext.getUserId()).willReturn(1L);
        Activity withdrawalActivity = ActivityFixture.특정_모임_탈퇴한_회원();
        given(activityRepository.findByMeetingIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(withdrawalActivity));

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> activityService.withdrawJoinAssemble(meetingId));
    }
}