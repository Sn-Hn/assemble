package com.assemble.meeting.service;

import com.assemble.commons.base.UserContext;
import com.assemble.fixture.PageableFixture;
import com.assemble.meeting.dto.request.MeetingLikeRequest;
import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.meeting.fixture.MeetingLikeFixture;
import com.assemble.meeting.repository.MeetingLikeRepository;
import com.assemble.meeting.repository.MeetingRepository;
import com.assemble.util.AuthenticationUtils;
import org.junit.jupiter.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("MeetingLikeService")
@ExtendWith(MockitoExtension.class)
class MeetingLikeServiceTest {

    @InjectMocks
    private MeetingLikeService meetingLikeService;

    @Mock
    private MeetingLikeRepository postLikeRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserContext userContext;

    @Test
    void 모임_좋아요() {
        // given
        given(postLikeRepository.findPostByUser(anyLong(), anyLong())).willReturn(Optional.empty());
        given(postLikeRepository.save(any())).willReturn(MeetingLikeFixture.좋아요_객체());
        given(meetingRepository.findById(any())).willReturn(Optional.of(MeetingFixture.모임()));
        given(userContext.getUserId()).willReturn(1L);
        AuthenticationUtils.setSecurityContextToUser(1L);
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_요청();

        // when
        boolean isLike = meetingLikeService.likePost(meetingLikeRequest);

        // then
        assertThat(isLike).isTrue();
    }

    @Test
    void 모임_좋아요_취소() {
        // given
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.of(MeetingLikeFixture.좋아요_객체()));
        given(meetingRepository.findById(any())).willReturn(Optional.of(MeetingFixture.모임()));
        given(userContext.getUserId()).willReturn(1L);
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_취소_요청();

        // when
        boolean isLike = meetingLikeService.cancelLikePost(meetingLikeRequest.getMeetingId());

        // then
        assertThat(isLike).isTrue();
    }

    @Test
    void 이미_좋아요_한_모임() {
        // given
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_요청();
        given(postLikeRepository.findPostByUser(any(), anyLong())).willReturn(Optional.of(MeetingLikeFixture.좋아요_객체()));
        AuthenticationUtils.setSecurityContextToUser(1L);

        // when
        boolean aleadyLikeByUser = meetingLikeService.isAleadyLikeByUser(meetingLikeRequest.getMeetingId());

        // then
        assertThat(aleadyLikeByUser).isTrue();
    }

    @Test
    void 좋아요_하지_않은_모임() {
        // given
        MeetingLikeRequest meetingLikeRequest = MeetingLikeFixture.모임_좋아요_요청();
        given(postLikeRepository.findPostByUser(anyLong(), anyLong())).willReturn(Optional.empty());
        AuthenticationUtils.setSecurityContextToUser(1L);

        // when
        boolean aleadyLikeByUser = meetingLikeService.isAleadyLikeByUser(meetingLikeRequest.getMeetingId());

        // then
        assertThat(aleadyLikeByUser).isFalse();
    }
    
    @Test
    void 좋아요_한_모임_목록_조회() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        Likes likes = MeetingLikeFixture.좋아요_객체();
        given(userContext.getUserId()).willReturn(1L);
        given(postLikeRepository.countByUserId(anyLong())).willReturn(1L);
        given(postLikeRepository.findAllByUserId(anyLong(), any())).willReturn(List.of(likes));

        // when
        Page<Meeting> postsByLike = meetingLikeService.getMeetingsByLike(pageable);

        // then
        Assertions.assertAll(
                () -> assertThat(postsByLike.getTotalElements()).isGreaterThan(0),
                () -> assertThat(postsByLike.get().count()).isGreaterThan(0),
                () -> assertThat(postsByLike.get().findFirst().get().getMeetingId()).isEqualTo(likes.getMeeting().getMeetingId())
        );
    }
}