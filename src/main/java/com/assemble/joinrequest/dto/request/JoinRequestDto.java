package com.assemble.joinrequest.dto.request;

import com.assemble.joinrequest.domain.JoinRequestStatus;
import com.assemble.joinrequest.entity.JoinRequest;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JoinRequestDto {

    @ApiModelProperty(value = "모임 ID", example = "1")
    @NotNull
    private Long postId;

    @ApiModelProperty(value = "가입 신청 메시지", example = "가입 신청합니다~")
    @NotNull
    private String joinRequestMessage;

    public JoinRequest toEntity(Long userId) {
        return new JoinRequest(
                new Post(postId),
                new User(userId),
                JoinRequestStatus.REQUEST,
                joinRequestMessage,
                null
        );
    }
}
