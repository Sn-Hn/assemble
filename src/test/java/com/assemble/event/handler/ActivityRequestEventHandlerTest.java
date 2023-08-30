package com.assemble.event.handler;

import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.activity.repository.ActivityRepository;
import com.assemble.join.entity.JoinRequest;
import com.assemble.join.fixture.JoinRequestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("JoinRequset Event Handler")
@ExtendWith(MockitoExtension.class)
class ActivityRequestEventHandlerTest {

    @InjectMocks
    private JoinRequestEventHandler joinRequestEventHandler;

    @Mock
    private ActivityRepository activityRepository;

    @Test
    void 모임_가입_신청_승인_시_모임_가입() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.승인된_회원();
        JoinRequestEvent event = new JoinRequestEvent(joinRequest);
        given(activityRepository.save(any())).willReturn(new Activity(event.getJoinRequest().getPost(), event.getJoinRequest().getUser(), ActivityStatus.NORMAL));

        // when
        Activity activity = joinRequestEventHandler.doJoinRequestEvent(event);

        // then
        assertAll(
                () -> assertThat(activity.getPost().getPostId()).isEqualTo(joinRequest.getPost().getPostId()),
                () -> assertThat(activity.getUser().getUserId()).isEqualTo(joinRequest.getUser().getUserId()),
                () -> assertThat(activity.getStatus()).isEqualTo(ActivityStatus.NORMAL)
        );
    }

}