package com.assemble.joinrequest.dto.response;

import com.assemble.joinrequest.domain.JoinRequestStatus;
import com.assemble.joinrequest.entity.JoinRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinResponse {

    private Long postId;

    private Long userId;

    private String status;

    private String message;

    public JoinResponse(JoinRequest joinRequest) {
        this(
                joinRequest.getPost().getPostId(),
                joinRequest.getUser().getUserId(),
                joinRequest.getStatus().toString(),
                JoinRequestStatus.REQUEST.toString().equals(joinRequest.getStatus()) ?
                joinRequest.getRequestMessage() : joinRequest.getRejectMessage()
        );
    }
}
