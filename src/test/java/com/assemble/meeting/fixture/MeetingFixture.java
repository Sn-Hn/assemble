package com.assemble.meeting.fixture;

import com.assemble.activity.domain.Activities;
import com.assemble.category.entity.Category;
import com.assemble.comment.domain.Comments;
import com.assemble.meeting.domain.*;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.meeting.dto.request.MeetingCreationRequest;
import com.assemble.meeting.dto.request.MeetingSearchRequest;
import com.assemble.meeting.dto.response.MeetingCreationResponse;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;

import java.io.IOException;
import java.util.ArrayList;

public class MeetingFixture {
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
    private static final String zipCode = "10011";
    private static final String roadNameAddress = "서울시 강남구 XXX번길";
    private static final String lotNumberAddress = "서울시 강남구 XXX-X";
    private static final String detailAddress = "XX건물 2층 201호";

    public static MeetingCreationRequest 모임_작성_사진_X() {
        return new MeetingCreationRequest(
                name,
                description,
                categoryId,
                zipCode,
                roadNameAddress,
                lotNumberAddress,
                detailAddress
        );
    }

    public static MeetingCreationRequest 모임_작성_사진_O() throws IOException {
        return new MeetingCreationRequest(
                name,
                description,
                categoryId,
                zipCode,
                roadNameAddress,
                lotNumberAddress,
                detailAddress
        );
    }

    public static MeetingCreationResponse 모임_작성_응답() {
        return new MeetingCreationResponse(
                name,
                description,
                categoryId,
                writerNickname,
                writer,
                hits,
                likeCount,
                new ArrayList<>(),
                MeetingStatus.PROGRESS.toString(),
                1,
                roadNameAddress,
                detailAddress);
    }

    public static Meeting 모임() {
        return new Meeting(
                meetingId,
                new MeetingName(name),
                new Description(description),
                new User(writer),
                hits,
                likeCount,
                new Comments(),
                new Category(categoryId, categoryName),
                new MeetingImages(),
                false,
                false,
                MeetingStatus.PROGRESS,
                new Activities(),
                주소()
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
                MeetingStatus.COMPLETED.toString()
        );
    }

    public static Address 주소() {
        return new Address("0000", "test 1번길", null, "test");
    }
}
