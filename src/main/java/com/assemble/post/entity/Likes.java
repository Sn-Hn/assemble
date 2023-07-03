package com.assemble.post.entity;

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
    @JoinColumn(name = "POST_ID")
    private Post post;

    protected Likes() {
    }

    public Likes(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
