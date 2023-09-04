package com.assemble.meeting.fixture;

import com.assemble.category.entity.Category;
import com.assemble.comment.domain.Comments;
import com.assemble.meeting.domain.Description;
import com.assemble.meeting.domain.MeetingStatus;
import com.assemble.meeting.domain.MeetingName;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.dto.response.MeetingCreationResponse;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;

import java.io.IOException;
import java.util.ArrayList;

public class PostFixture {
    private static final Long meetingId = 1L;
    private static final String name = "스터디 모집";
    private static final String modifiedTitle = "스터디 모집 1명 (수정)";
    private static final String description = "스터디 모집합니다.";
    private static final String modifiedContents = "스터디 2명 모집합니다. (수정)";
    private static final Long categoryId = 1L;
    private static final String writerNickname = "개발자";
    private static final Long writer = 1L;
    private static final Long hits = 2L;
    private static final Long likeCount = 2L;
    private static final int personnelNumber = 0;
    private static final int expectedPeriod = 0;
    private static final String searchByTitle = "name";
    private static final String searchQueryTitle = "모임";
    private static final String searchByContents = "description";
    private static final String searchQueryContents = "설명";
    private static final Long searchQueryUserId = 1L;
    private static final String searchByWriter = "writer";
    private static final String categoryName = "카테고리 이름";

    public static MeetingCreationRequest 모임_작성_사진_X() {
        return new MeetingCreationRequest(
                name,
                description,
                categoryId,
                personnelNumber,
                expectedPeriod
        );
    }

    public static MeetingCreationRequest 모임_작성_사진_O() throws IOException {
        return new MeetingCreationRequest(
                name,
                description,
                categoryId,
                personnelNumber,
                expectedPeriod
        );
    }

    public static MeetingCreationResponse 모임_작성_응답() {
        return new MeetingCreationResponse(name, description, categoryId, writerNickname, writer, hits, likeCount, personnelNumber, expectedPeriod, new ArrayList<>(), MeetingStatus.PROGRESS.toString());
    }

    public static Meeting 모임() {
        return new Meeting(
                meetingId,
                new MeetingName(name),
                new Description(description),
                new User(writer),
                hits,
                likeCount,
                personnelNumber,
                new Comments(),
                expectedPeriod,
                new Category(categoryId, categoryName),
                new ArrayList<>(),
                false,
                false,
                MeetingStatus.PROGRESS
        );
    }

    public static MeetingSearchRequest 모임_이름_검색() {
        return new MeetingSearchRequest(searchQueryTitle, searchByTitle, null);
    }

    public static MeetingSearchRequest 모임_설명_검색() {
        return new MeetingSearchRequest(searchQueryContents, searchByContents, null);
    }

    public static MeetingSearchRequest 모임_작성자_검색() {
        return new MeetingSearchRequest(searchQueryUserId.toString(), searchByWriter, null);
    }

    public static ModifiedMeetingRequest 모임_수정() {
        return new ModifiedMeetingRequest(
                meetingId,
                modifiedTitle,
                modifiedContents,
                categoryId,
                personnelNumber,
                expectedPeriod,
                MeetingStatus.COMPLETED.toString()
        );
    }
}
