package com.assemble.comment.entity;

import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.commons.base.BaseDateEntity;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE comment SET is_deleted = 'Y' WHERE comment_Id = ?")
@Where(clause = "is_deleted = 'N'")
public class Comment extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETING_ID")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String contents;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    protected Comment() {}

    public Comment(Long commentId) {
        this.commentId = commentId;
    }

    public Comment(Long meetingId, Long userId, String contents) {
        this(null, new Meeting(meetingId), new User(userId), contents, false);
    }

    public void modifyComment(ModifiedCommentRequest modifiedCommentRequest) {
        this.contents = modifiedCommentRequest.getContents();
    }
}
