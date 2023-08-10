package com.assemble.post.dto.request;

import com.assemble.commons.base.BaseRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeRequest {

    @ApiModelProperty(value = "게시글 ID", required = true, example = "1")
    private Long postId;

    public Likes toEntity(Long userId) {
        return new Likes(new User(userId), new Post(this.postId));
    }
}
