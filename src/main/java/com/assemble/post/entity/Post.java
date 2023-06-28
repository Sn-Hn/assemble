package com.assemble.post.entity;

import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
public class Post extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    private Long hits;

    @OneToOne
    private Like likes;

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
