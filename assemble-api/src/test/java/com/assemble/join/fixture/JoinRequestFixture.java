package com.assemble.join.fixture;

import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.join.dto.request.JoinRequestDto;
import com.assemble.join.entity.JoinRequest;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.user.fixture.UserFixture;

public class JoinRequestFixture {
    private static final Long joinRequestId = 1L;
    private static final Long meetingId = 1L;
    private static final Long userId = 2L;
    private static final String requestMessage = "가입 신청합니다 ! 받아주세요 ~";
    private static final String rejectMessage = "죄송합니다 ~";
    private static final String fcmToken = "fcm token";

    public static JoinRequest 정상_신청_회원() {
        return 가입_처리_응답(JoinRequestStatus.REQUEST);
    }

    public static JoinRequest 승인된_회원() {
        return 가입_처리_응답(JoinRequestStatus.APPROVAL);
    }

    public static JoinRequest 거절된_회원() {
        return 가입_처리_응답(JoinRequestStatus.REJECT);
    }

    public static JoinRequest 차단된_회원() {
        return 가입_처리_응답(JoinRequestStatus.BLOCK);
    }

    public static JoinRequest 가입_처리_응답(JoinRequestStatus status) {
        return new JoinRequest(
                MeetingFixture.모임(),
                UserFixture.관리자(),
                status,
                null
        );
    }

    public static JoinRequestDto 가입_신청() {
        return new JoinRequestDto(meetingId, requestMessage, fcmToken);
    }

    public static JoinRequestAnswer 가입_요청_승인() {
        return 가입_요청_처리(JoinRequestStatus.APPROVAL.toString(), null);
    }

    public static JoinRequestAnswer 가입_요청_거절() {
        return 가입_요청_처리(JoinRequestStatus.REJECT.toString(), rejectMessage);
    }

    public static JoinRequestAnswer 가입_요청_차단() {
        return 가입_요청_처리(JoinRequestStatus.BLOCK.toString(), null);
    }

    public static JoinRequestAnswer 가입_요청_처리(String status, String message) {
        return new JoinRequestAnswer(joinRequestId, status, message, fcmToken);
    }
}
