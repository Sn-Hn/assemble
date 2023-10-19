package com.assemble.noti.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String message;

    private boolean isRead;

    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
