package com.assemble.join.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class JoinRequestAnswer {
    private Long joinRequestId;

    private String status;

    private String message;
}
