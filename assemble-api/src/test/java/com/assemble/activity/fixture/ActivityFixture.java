package com.assemble.activity.fixture;

import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.dto.request.DismissUserRequest;
import com.assemble.activity.entity.Activity;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.user.fixture.UserFixture;

public class ActivityFixture {

    public static Activity 특정_모임_활동_중인_회원() {
        return 특정_모임_활동_회원(ActivityStatus.NORMAL);
    }

    public static Activity 특정_모임_탈퇴한_회원() {
        return 특정_모임_활동_회원(ActivityStatus.WITHDRAWAL);
    }

    public static DismissUserRequest 회원_강퇴_요청() {
        return new DismissUserRequest(2L, 2L);
    }

    private static Activity 특정_모임_활동_회원(ActivityStatus withdrawal) {
        return new Activity(
                MeetingFixture.모임(),
                UserFixture.회원(),
                withdrawal
        );
    }
}
