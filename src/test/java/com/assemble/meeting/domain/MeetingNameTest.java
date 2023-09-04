package com.assemble.meeting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Title")
class MeetingNameTest {

    @Test
    void 제목_빈값_검증() {
        // given
        String empty = "";

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new MeetingName(empty));

    }

    @Test
    void 제목_NULL_검증() {
        // given, when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new MeetingName(null));
    }

    @Test
    void 제목_정상_검증() {
        // given
        String nameString = "name";

        // when
        MeetingName meetingName = new MeetingName(nameString);

        // then
        assertThat(meetingName.getValue()).isEqualTo(nameString);

    }
}