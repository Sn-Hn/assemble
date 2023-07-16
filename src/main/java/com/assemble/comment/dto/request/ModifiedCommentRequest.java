package com.assemble.comment.dto.request;

import com.assemble.comment.entity.Comment;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedCommentRequest {

    private Long postId;

    private Long userId;

    private Long commentId;

    private String contents;

    public Comment toEntity() {
        return new Comment(commentId, new Post(postId), new User(userId), contents, false);
    }
}
