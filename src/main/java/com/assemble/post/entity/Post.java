package com.assemble.post.entity;

import com.assemble.category.domain.CategoryName;
import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.file.entity.AttachedFile;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.user.entity.User;
import com.assemble.user.entity.UserImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ColumnDefault("0")
    private Long hits;

    @ColumnDefault("0")
    private Long likes;

    @ColumnDefault("0")
    private int personnelNumber;

    @Embedded
    private Comments comments;

    @ColumnDefault("0")
    private int expectedPeriod;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<PostImage> profiles = new ArrayList<>();

    protected Post() {}

    public Post(Long postId) {
        this.postId = postId;
    }

    public Post(Title title, Contents contents, User user, int personnelNumber, int expectedPeriod, Category category) {
        this (null, title, contents, user, 0L, 0L, personnelNumber, null, expectedPeriod, category, new ArrayList<>());
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new PostImage(this, this.user, file));
    }

    public void modifyPost(ModifiedPostRequest modifiedPostRequest) {
        this.title = new Title(modifiedPostRequest.getTitle());
        this.contents = new Contents(modifiedPostRequest.getContents());
        this.category = new Category(new CategoryName(modifiedPostRequest.getCategory()));
        this.personnelNumber = modifiedPostRequest.getPersonnelNumber();
        this.expectedPeriod = modifiedPostRequest.getExpectedPeriod();
    }

    public void increaseHits() {
        this.hits ++;
    }

    public void increaseLikes() {
        this.likes ++;
    }

    public void decreaseLikes() {
        this.likes --;
    }
}
