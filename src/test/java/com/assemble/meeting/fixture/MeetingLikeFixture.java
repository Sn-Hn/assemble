package com.assemble.meeting.fixture;

import com.assemble.meeting.dto.request.MeetingLikeRequest;
import com.assemble.meeting.entity.Likes;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;

public class MeetingLikeFixture {
    private static final Long meetingId = 1L;
    private static final Long userId = 2L;
    private static final Long aleadyLikeUserId = 1L;

    public static MeetingLikeRequest 모임_좋아요_요청() {
        return new MeetingLikeRequest(meetingId);
    }

    public static MeetingLikeRequest 모임_좋아요_취소_요청() {
        return new MeetingLikeRequest(meetingId);
    }

    public static Likes 좋아요_객체() {
        return new Likes(new User(userId), new Meeting(meetingId));
    }
}
