package com.assemble.comment.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.post.entity.Post;
import lombok.AllArgsConstructor;

import javax.persistence.*;

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
}
