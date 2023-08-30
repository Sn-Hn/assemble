package com.assemble.event.handler;

import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.join.domain.JoinStatus;
import com.assemble.join.entity.Join;
import com.assemble.join.repository.JoinRepository;
import com.assemble.joinrequest.entity.JoinRequest;
import com.assemble.joinrequest.fixture.JoinRequestFixture;
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
class JoinRequestEventHandlerTest {

    @InjectMocks
    private JoinRequestEventHandler joinRequestEventHandler;

    @Mock
    private JoinRepository joinRepository;

    @Test
    void 모임_가입_신청_승인_시_모임_가입() {
        // given
        JoinRequest joinRequest = JoinRequestFixture.승인된_회원();
        JoinRequestEvent event = new JoinRequestEvent(joinRequest);
        given(joinRepository.save(any())).willReturn(new Join(event.getJoinRequest().getPost(), event.getJoinRequest().getUser(), JoinStatus.NORMAL));

        // when
        Join join = joinRequestEventHandler.doJoinRequestEvent(event);

        // then
        assertAll(
                () -> assertThat(join.getPost().getPostId()).isEqualTo(joinRequest.getPost().getPostId()),
                () -> assertThat(join.getUser().getUserId()).isEqualTo(joinRequest.getUser().getUserId()),
                () -> assertThat(join.getJoinStatus()).isEqualTo(JoinStatus.NORMAL)
        );
    }

}