package com.assemble.activity.fixture;

import com.assemble.activity.domain.ActivityStatus;
import com.assemble.activity.entity.Activity;
import com.assemble.meeting.fixture.PostFixture;
import com.assemble.user.fixture.UserFixture;

public class ActivityFixture {

    public static Activity 특정_모임_활동_중인_회원() {
        return 특정_모임_활동_회원(ActivityStatus.NORMAL);
    }

    public static Activity 특정_모임_탈퇴한_회원() {
        return 특정_모임_활동_회원(ActivityStatus.WITHDRAWAL);
    }

    private static Activity 특정_모임_활동_회원(ActivityStatus withdrawal) {
        return new Activity(
                PostFixture.모임(),
                UserFixture.회원(),
                withdrawal
        );
    }
}
