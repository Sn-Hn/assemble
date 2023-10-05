package com.assemble.event.publish;

import com.assemble.meeting.entity.Meeting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MeetingEvent {

    private final Meeting meeting;

}
