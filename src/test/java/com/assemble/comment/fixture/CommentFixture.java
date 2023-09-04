package com.assemble.comment.fixture;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.entity.Comment;
import com.assemble.meeting.fixture.PostFixture;
import com.assemble.user.entity.User;

public class CommentFixture {

    private static final Long meetingId = 1L;
    private static final Long userId = 1L;
    private static final Long commentId = 1L;
    private static final String contents = "댓글입니다!!!!";

    public static CommentCreationRequest 댓글_생성_요청() {
        return new CommentCreationRequest(meetingId, contents);
    }

    public static Comment 댓글_조회() {
        return new Comment(commentId, PostFixture.모임(), new User(userId), contents, false);
    }

    public static ModifiedCommentRequest 댓글_수정_요청() {
        return new ModifiedCommentRequest(commentId, contents);
    }
}
