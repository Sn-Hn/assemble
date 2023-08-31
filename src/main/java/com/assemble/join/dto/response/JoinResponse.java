package com.assemble.join.dto.response;

import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.entity.JoinRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class JoinResponse {

    private Long joinRequestId;

    private Long postId;

    private Long userId;

    private String nickname;

    private String status;

    private String message;

    private LocalDateTime createdDate;

    public JoinResponse(JoinRequest joinRequest) {
        this(
                joinRequest.getId(),
                joinRequest.getPost().getPostId(),
                joinRequest.getUser().getUserId(),
                joinRequest.getUser().getNickname(),
                joinRequest.getStatus().toString(),
                JoinRequestStatus.REQUEST.toString().equals(joinRequest.getStatus()) ?
                joinRequest.getRequestMessage() : joinRequest.getRejectMessage(),
                joinRequest.getCreatedDate()
        );
    }
}
