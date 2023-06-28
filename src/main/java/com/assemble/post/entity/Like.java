package com.assemble.post.entity;

import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    protected Like() {
    }
}
