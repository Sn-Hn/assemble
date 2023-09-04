package com.assemble.event.handler;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.PostEvent;
import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Post Event Handler test")
@ExtendWith(MockitoExtension.class)
class MeetingEventHandlerTest {

    @InjectMocks
    private PostEventHandler postEventHandler;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 모임_생성_시_모임_가입() {
        // given
        Meeting meeting = MeetingFixture.모임();
        Long userId = 1L;
        PostEvent postEvent = new PostEvent(meeting);
        given(userContext.getUserId()).willReturn(userId);
        given(activityRepository.save(any())).willReturn(new Activity(meeting, new User(userId), ActivityStatus.NORMAL));

        // when
        Activity activity = postEventHandler.doPostEvent(postEvent);

        // then
        assertAll(
                () -> assertThat(activity.getMeeting().getMeetingId()).isEqualTo(meeting.getMeetingId()),
                () -> assertThat(activity.getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(activity.getStatus()).isEqualTo(ActivityStatus.NORMAL)
        );
    }
}