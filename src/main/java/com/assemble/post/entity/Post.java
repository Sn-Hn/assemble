package com.assemble.post.entity;

import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
public class Post extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    private Long hits;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Embedded
    private Comments comments;

    private int personnelNumber;

    private int expectedPeriod;

    @ManyToOne
    private Category category;

    protected Post() {}
}
