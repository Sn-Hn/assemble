package com.assemble.comment.dto.request;

import com.assemble.comment.entity.Comment;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedCommentRequest {

    private Long commentId;

    private String contents;
}
