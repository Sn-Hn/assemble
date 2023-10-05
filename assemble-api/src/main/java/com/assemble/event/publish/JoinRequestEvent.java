package com.assemble.event.publish;

import com.assemble.join.entity.JoinRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinRequestEvent {

    private final JoinRequest joinRequest;

}
