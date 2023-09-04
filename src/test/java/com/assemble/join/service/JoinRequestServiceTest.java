package com.assemble.join.service;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.fixture.PageableFixture;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.entity.JoinRequest;
import com.assemble.join.fixture.JoinRequestFixture;
import com.assemble.join.repository.JoinRequestRepository;
import com.assemble.post.entity.Post;
import com.assemble.post.fixture.PostFixture;
import com.assemble.post.repository.PostRepository;
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
    private PostRepository postRepository;

    @Mock
    private UserContext userContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void 모임_가입_신청() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestFixture.가입_신청();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(joinRequestRepository.save(any())).willReturn(JoinRequestFixture.정상_신청_회원());
        given(userContext.getUserId()).willReturn(2L);

        // when
        JoinRequest joinRequest = joinRequestService.requestJoinToAssemble(joinRequestDto);

        // then
        assertAll(
                () -> assertThat(joinRequest.getPost().getPostId()).isEqualTo(joinRequestDto.getPostId()),
                () -> assertThat(joinRequest.getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REQUEST)
        );
    }

    @Test
    void 모임_가입_승인_검증() {
        // given
        String status = "APPROVAL";
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        JoinRequest joinRequest = joinRequestService.responseJoinFromAssemble(joinRequestAnswer);

        // then
        assertAll(
                () -> assertThat(joinRequest.getPost().getUser().getUserId()).isEqualTo(userContext.getUserId()),
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
        JoinRequest joinRequest = joinRequestService.responseJoinFromAssemble(joinRequestAnswer);

        // then
        assertAll(
                () -> assertThat(joinRequest.getPost().getUser().getUserId()).isEqualTo(userContext.getUserId()),
                () -> assertThat(joinRequest.getStatus().toString()).isEqualTo(status)
        );

        verify(eventPublisher, times(0)).publishEvent(any(JoinRequestEvent.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"APPROVAL", "REJECT", "BLOCK"})
    void 모임_생성자가_아니면_모임_가입_처리_안됨(String status) {
        // given
        JoinRequestAnswer joinRequestAnswer = JoinRequestFixture.가입_요청_처리(status, null);
        // 모임 생성자 UserId=1
        given(joinRequestRepository.findById(anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        // 로그인한 회원 UserId=2
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.responseJoinFromAssemble(joinRequestAnswer));

    }

    @Test
    void 정상_모임_가입_취소() {
        // given
        Post post = PostFixture.게시글();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(2L);

        // when
        boolean isCancel = joinRequestService.cancelJoinOfAssemble(post.getPostId());

        // then
        assertThat(isCancel).isTrue();
    }

    @Test
    void 가입_취소_본인_아닌_경우_검증() {
        // given
        Post post = PostFixture.게시글();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.cancelJoinOfAssemble(post.getPostId()));
    }

    @Test
    void 가입_취소_시_신청_하지_않은_경우_검증() {
        // given
        Post post = PostFixture.게시글();
        given(joinRequestRepository.findByAssembleIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(JoinRequestFixture.거절된_회원()));
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> joinRequestService.cancelJoinOfAssemble(post.getPostId()));
    }

    @Test
    void 모임_가입_신청_조회() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Post post = PostFixture.게시글();
        Long postId = post.getPostId();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(joinRequestRepository.findAllByPostId(anyLong())).willReturn(List.of(JoinRequestFixture.정상_신청_회원()));
        given(userContext.getUserId()).willReturn(1L);

        // when
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequests(postId);

        // then
        assertAll(
                () -> assertThat(joinRequests).isNotEmpty(),
                () -> assertThat(joinRequests.size()).isEqualTo(1),
                () -> assertThat(joinRequests.stream().findFirst().get().getPost().getPostId()).isEqualTo(postId),
                () -> assertThat(joinRequests.stream().findFirst().get().getPost().getUser().getUserId()).isEqualTo(post.getUser().getUserId())
        );
    }

    @Test
    void 모임장_아니면_가입_신청_조회_불가능() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Post post = PostFixture.게시글();
        Long postId = post.getPostId();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(userContext.getUserId()).willReturn(2L);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinRequestService.getJoinRequests(postId));

    }
}