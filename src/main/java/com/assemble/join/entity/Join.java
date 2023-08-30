package com.assemble.join.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.join.domain.JoinStatus;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "JOINS")
public class Join extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Enumerated(EnumType.STRING)
    private JoinStatus joinStatus;

    protected Join() {}

    public Join(Post post, User user, JoinStatus joinStatus) {
        this.post = post;
        this.user = user;
        this.joinStatus = joinStatus;
    }

    @Override
    public String toString() {
        return "Join{" +
                "id=" + id +
                ", post=" + post +
                ", user=" + user +
                ", joinStatus=" + joinStatus +
                '}';
    }
}
