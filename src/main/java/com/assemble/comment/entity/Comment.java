package com.assemble.comment.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
public class Comment extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    protected Comment() {}

    public Comment(String comment, Long postId) {
        this(null, comment, new Post(postId));
    }
}
