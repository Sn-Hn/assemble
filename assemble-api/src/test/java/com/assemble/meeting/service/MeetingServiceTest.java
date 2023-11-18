package com.assemble.meeting.service;

import com.assemble.commons.base.UserContext;
import com.assemble.event.publish.MeetingEvent;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.meeting.repository.MeetingLikeRepository;
import com.assemble.meeting.repository.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("MeetingService")
@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @InjectMocks
    private MeetingService meetingService;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserContext userContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MeetingLikeRepository meetingLikeRepository;

    @Test
    void 모임_작성() {
        // given
        MeetingCreationRequest meetingCreationRequest = MeetingFixture.모임_작성_사진_X();
        given(meetingRepository.save(any()))
                .willReturn(MeetingFixture.모임());
        given(userContext.getUserId()).willReturn(1L);

        // when
        Meeting response = meetingService.createPost(meetingCreationRequest);

        // then
        assertAll(
                () -> assertThat(response.getName().getValue()).isEqualTo(meetingCreationRequest.getName()),
                () -> assertThat(response.getDescription().getValue()).isEqualTo(meetingCreationRequest.getDescription()),
                () -> assertThat(response.getCategory().getId()).isEqualTo(meetingCreationRequest.getCategoryId()),
                () -> assertThat(response.getUser().getUserId()).isEqualTo(userContext.getUserId())
        );

        verify(eventPublisher, times(1)).publishEvent(any(MeetingEvent.class));
    }

    @Test
    void 모임_목록_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 12);
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_이름_검색();
        given(meetingRepository.findAllBySearch(any(), anyLong(), any())).willReturn(List.of(MeetingFixture.모임()));

        // when
        Page<Meeting> posts = meetingService.getMeetings(meetingSearchRequest, pageable);

        // then
        assertAll(
                () -> assertThat(posts).isNotNull(),
                () -> assertThat(posts).hasSizeGreaterThan(0)
        );
    }

    @Test
    void 모임_상세_조회() {
        // given
        Meeting mockMeeting = MeetingFixture.모임();
        given(meetingRepository.findByIdForUpdate(any())).willReturn(Optional.of(mockMeeting));
        given(meetingLikeRepository.findPostByUser(any(), any())).willReturn(Optional.empty());

        // when
        Meeting meeting = meetingService.getMeeting(1L);

        // then
        assertAll(
                () -> assertThat(meeting).isNotNull(),
                () -> assertThat(meeting.getMeetingId()).isEqualTo(1L)
        );
    }

    @Test
    void 모임_수정() {
        // given
        given(meetingRepository.findById(any())).willReturn(Optional.of(MeetingFixture.모임()));
        given(userContext.getUserId()).willReturn(MeetingFixture.모임().getUser().getUserId());
        ModifiedMeetingRequest modifiedMeetingRequest = MeetingFixture.모임_수정();

        // when
        Meeting meeting = meetingService.modifyPost(modifiedMeetingRequest);

        // then
        assertAll(
                () -> assertThat(meeting).isNotNull(),
                () -> assertThat(meeting.getName().getValue()).isEqualTo(modifiedMeetingRequest.getName()),
                () -> assertThat(meeting.getDescription().getValue()).isEqualTo(modifiedMeetingRequest.getDescription())
        );
    }

    @Test
    void 모임_삭제() {
        // given
        Meeting meeting = MeetingFixture.모임();
        given(meetingRepository.findById(any())).willReturn(Optional.of(meeting)).willReturn(null);
        given(userContext.getUserId()).willReturn(meeting.getUser().getUserId());

        // when
        boolean isDeletedPost = meetingService.deletePost(meeting.getMeetingId());

        // then
        assertThat(isDeletedPost).isTrue();
    }

    @Test
    void 특정_회원이_작성한_모임_조회() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 12);
        List<Meeting> meetings = List.of(MeetingFixture.모임());
        given(meetingRepository.countByUserId(any())).willReturn(1L);
        given(meetingRepository.findAllByUserId(anyLong(), anyLong(), any())).willReturn(meetings);

        // when
        Page<Meeting> postsByUser = meetingService.getMeetingsByUser(userId, pageable);

        // then
        assertAll(
                () -> assertThat(postsByUser).isNotEmpty(),
                () -> assertThat(postsByUser.get().count()).isEqualTo(meetings.size()),
                () -> assertThat(postsByUser.get().findFirst().get()
                        .getUser()
                        .getUserId()).isEqualTo(userId)
        );
    }
}