package com.assemble.meeting.entity;

import com.assemble.commons.base.BaseDateEntity;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;


@Getter
@AllArgsConstructor
@Entity
public class Likes extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "MEETING_ID")
    private Meeting meeting;

    protected Likes() {
    }

    public Likes(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }
}
