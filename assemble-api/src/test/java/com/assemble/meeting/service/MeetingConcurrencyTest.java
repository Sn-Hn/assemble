package com.assemble.meeting.service;

import com.assemble.annotation.TestCustomProperty;
import com.assemble.commons.base.UserContext;
import com.assemble.meeting.entity.Meeting;
import com.assemble.meeting.fixture.MeetingFixture;
import com.assemble.util.AuthenticationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestCustomProperty
@SpringBootTest
public class MeetingConcurrencyTest {

    @Autowired
    private MeetingService meetingService;

    @Test
    void 상세_조회_시_조회수_동시성_검증() throws InterruptedException {
        Long meetingId = 3L;
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AuthenticationUtils.setSecurityContextToUser(1L);

        for (int i = 0; i < threadCount; i++) {
            Long userId = Long.valueOf(i);
            executorService.submit(() -> {
                try {
                    AuthenticationUtils.setSecurityContextToUser(userId);
                    meetingService.getMeeting(meetingId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Meeting meeting = meetingService.getMeeting(meetingId);
        assertAll(
                () -> assertThat(meeting.getMeetingId()).isEqualTo(meetingId),
                () -> assertThat(meeting.getHits()).isEqualTo(threadCount + 1)
        );
    }

}
