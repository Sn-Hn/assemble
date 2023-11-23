package com.assemble.activity.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.activity.domain.ActivityStatus;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
public class Activity extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETING_ID")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @Transient
    private boolean isHost = false;

    protected Activity() {}

    public Activity(Meeting meeting, User user, ActivityStatus status) {
        this.meeting = meeting;
        this.user = user;
        this.status = status;
    }

    public void withdraw(Long userId) {
        validateWithdrawal(userId);
        this.status = ActivityStatus.WITHDRAWAL;
    }

    public void validateWithdrawal(Long userId) {
        if (this.status.equals(ActivityStatus.WITHDRAWAL)) {
            throw new IllegalStateException("이미 탈퇴한 모임입니다.");
        }

        if (this.meeting.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("모임장은 탈퇴할 수 없습니다.");
        }
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isActivityUser(Long userId) {
        if (this.user.getUserId().equals(userId) && this.status.equals(ActivityStatus.NORMAL)) {
            return true;
        }

        return false;
    }

    public void validateAlreadyActivityUser(Long userId) {
        if (isActivityUser(userId)) {
            throw new IllegalStateException("이미 활동 중인 회원입니다.");
        }
    }

    public void dismiss() {
        this.status = ActivityStatus.DISMISS;
    }
}
