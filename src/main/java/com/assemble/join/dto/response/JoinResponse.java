package com.assemble.join.dto.response;

import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.join.entity.JoinRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinResponse {

    private Long joinRequestId;

    private Long postId;

    private Long userId;

    private String status;

    private String message;

    public JoinResponse(JoinRequest joinRequest) {
        this(
                joinRequest.getId(),
                joinRequest.getPost().getPostId(),
                joinRequest.getUser().getUserId(),
                joinRequest.getStatus().toString(),
                JoinRequestStatus.REQUEST.toString().equals(joinRequest.getStatus()) ?
                joinRequest.getRequestMessage() : joinRequest.getRejectMessage()
        );
    }
}
