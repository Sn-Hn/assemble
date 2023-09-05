package com.assemble.join.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.commons.exception.UserBlockException;
import com.assemble.join.dto.request.JoinRequestAnswer;
import com.assemble.meeting.entity.Meeting;
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

    @JoinColumn(name = "meetingId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting meeting;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private JoinRequestStatus status;

    private String requestMessage;

    protected JoinRequest() {
    }

    public JoinRequest(Meeting meeting, User user, JoinRequestStatus status, String requestMessage) {
        this.meeting = meeting;
        this.user = user;
        this.status = status;
        this.requestMessage = requestMessage;
    }

    public void answerJoinRequest(JoinRequestAnswer joinRequestAnswer, Long userId) {
        validateAssembleCreator(userId);
        validateAlreadyAnswerJoinRequest();
        this.status = JoinRequestStatus.valueOf(joinRequestAnswer.getStatus());
    }

    public void cancelJoinRequest(Long userId) {
        validateEqualRequestUser(userId);
        validateNotExistJoinRequest();
        this.status = JoinRequestStatus.CANCEL;
    }

    public boolean isApproval() {
        return JoinRequestStatus.APPROVAL.equals(this.status);
    }

    public void mapBlockToRequest() {
        this.status = this.status == JoinRequestStatus.BLOCK ? JoinRequestStatus.REQUEST : this.status;
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
        if (!this.meeting.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("모임 생성자만 접근할 수 있습니다.");
        }
    }

    private void validateEqualRequestUser(Long userId) {
        if (!getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("신청한 회원이 아닙니다.");
        }
    }

    private void validateAlreadyAnswerJoinRequest() {
        if (this.status.equals(JoinRequestStatus.APPROVAL) || this.status.equals(JoinRequestStatus.REJECT)) {
            throw new IllegalStateException("이미 처리된 회원입니다.");
        }
    }

    public void validateBlock(String updateStatus) {
        if (this.status.equals(JoinRequestStatus.BLOCK) && !JoinRequestStatus.REJECT.toString().equals(updateStatus)) {
            throw new IllegalStateException("차단된 회원입니다.");
        }
    }
}
