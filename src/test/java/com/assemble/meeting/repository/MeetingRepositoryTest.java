package com.assemble.meeting.repository;

import com.assemble.annotation.CustomRepositoryTest;
import com.assemble.fixture.PageableFixture;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("PostRepository")
@CustomRepositoryTest
class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    void 모임_목록_제목_검색() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_이름_검색();
        Long myUserId = 1L;

        // when
        long count = meetingRepository.count();
        List<Meeting> allMeetingBySearches = meetingRepository.findAllBySearch(meetingSearchRequest, myUserId, pageable);
        Page<Meeting> posts = new PageImpl<>(allMeetingBySearches, pageable, count);

        Meeting searchMeeting = posts.get().filter(post -> post.getName().getValue().contains(meetingSearchRequest.getSearchQuery()))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allMeetingBySearches).isNotEmpty(),
                () -> assertThat(searchMeeting).isNotNull(),
                () -> assertThat(searchMeeting.getName().getValue()).contains(meetingSearchRequest.getSearchQuery())
        );
    }

    @Test
    void 모임_목록_내용_검색() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_설명_검색();
        Long myUserId = 1L;

        // when
        long count = meetingRepository.count();
        List<Meeting> allMeetingBySearches = meetingRepository.findAllBySearch(meetingSearchRequest, myUserId, pageable);
        Page<Meeting> posts = new PageImpl<>(allMeetingBySearches, pageable, count);
        Meeting searchMeeting = posts.get().filter(post -> post.getDescription().getValue().contains(meetingSearchRequest.getSearchQuery()))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allMeetingBySearches).isNotEmpty(),
                () -> assertThat(searchMeeting).isNotNull(),
                () -> assertThat(searchMeeting.getDescription().getValue()).contains(meetingSearchRequest.getSearchQuery())
        );
    }

    @Test
    void 모임_목록_작성자_조회() {
        // given
        Pageable pageable = PageableFixture.pageable_생성_기본_정렬();
        MeetingSearchRequest meetingSearchRequest = MeetingFixture.모임_작성자_검색();
        Long userId = 1L;

        // when
        long count = meetingRepository.count();
        List<Meeting> allMeetingBySearches = meetingRepository.findAllBySearch(meetingSearchRequest, userId, pageable);
        Page<Meeting> posts = new PageImpl<>(allMeetingBySearches, pageable, count);
        Meeting searchMeeting = posts.get().filter(post -> post.getUser().getUserId().equals(Long.valueOf(meetingSearchRequest.getSearchQuery())))
                .findFirst().orElseThrow();

        // then
        assertAll(
                () -> assertThat(allMeetingBySearches).isNotEmpty(),
                () -> assertThat(searchMeeting).isNotNull(),
                () -> assertThat(searchMeeting.getUser().getUserId()).isEqualTo(Long.valueOf(meetingSearchRequest.getSearchQuery()))
        );
    }

    @Test
    void 모임_삭제_검증() {
        // given
        Meeting meeting = MeetingFixture.모임();

        // when
        meetingRepository.delete(meeting);
        boolean isDeletedPost = meetingRepository.findById(meeting.getMeetingId()).isPresent();

        // then
        assertThat(isDeletedPost).isFalse();
    }
}