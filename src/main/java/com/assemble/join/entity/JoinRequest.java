package com.assemble.join.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.commons.exception.UserBlockException;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.post.entity.Post;
import com.assemble.join.domain.JoinRequestStatus;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Entity
public class JoinRequest extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private JoinRequestStatus status;

    private String requestMessage;

    private String rejectMessage;

    protected JoinRequest() {
    }

    public JoinRequest(Post post, User user, JoinRequestStatus status, String requestMessage, String rejectMessage) {
        this.post = post;
        this.user = user;
        this.status = status;
        this.requestMessage = requestMessage;
        this.rejectMessage = rejectMessage;
    }

    public void answerJoinRequest(JoinRequestAnswer joinRequestAnswer, Long userId) {
        validateAssembleCreator(userId);
        this.status = JoinRequestStatus.valueOf(joinRequestAnswer.getStatus());
        this.rejectMessage = JoinRequestStatus.REJECT.toString().equals(joinRequestAnswer.getStatus()) ? joinRequestAnswer.getMessage() : null;
    }

    public void cancelJoinRequest(Long userId) {
        validateEqualRequestUser(userId);
        validateNotExistJoinRequest();
        this.status = JoinRequestStatus.CANCEL;
    }

    public void validateAlreadyJoinRequest() {
        if (JoinRequestStatus.REQUEST.equals(this.getStatus())) {
            throw new IllegalStateException("이미 가입 신청된 회원입니다.");
        }
    }

    private void validateNotExistJoinRequest() {
        if (!JoinRequestStatus.REQUEST.equals(this.getStatus())) {
            throw new IllegalStateException("가입 신청하지 않았습니다.");
        }
    }

    public void validateAnswerStatusOfJoinRequest() {
        if (JoinRequestStatus.APPROVAL.equals(this.getStatus())) {
            throw new IllegalStateException("승인된 회원입니다.");
        }

        if (JoinRequestStatus.BLOCK.equals(this.getStatus())) {
            throw new UserBlockException();
        }
    }

    private void validateAssembleCreator(Long userId) {
        if (!this.post.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("모임 생성자만 접근할 수 있습니다.");
        }
    }

    private void validateEqualRequestUser(Long userId) {
        if (!getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("신청한 회원이 아닙니다.");
        }
    }
}
