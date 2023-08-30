package com.assemble.post.entity;

import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.PostStatus;
import com.assemble.post.domain.Title;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE post SET is_deleted = 'Y' WHERE post_Id = ?")
@Where(clause = "is_deleted = 'N'")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<PostImage> profiles = new ArrayList<>();

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    @Transient
    private boolean isLike;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    protected Post() {}

    public Post(Long postId) {
        this.postId = postId;
    }

    public Post(Title title, Contents contents, User user, int personnelNumber, int expectedPeriod, Category category) {
        this (null, title, contents, user, 0L, 0L, personnelNumber, null, expectedPeriod, category, new ArrayList<>(), false, false, PostStatus.PROGRESS);
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new PostImage(this, this.user, file));
    }

    public void modifyPost(ModifiedPostRequest modifiedPostRequest) {
        this.title = new Title(modifiedPostRequest.getTitle());
        this.contents = new Contents(modifiedPostRequest.getContents());
        this.personnelNumber = modifiedPostRequest.getPersonnelNumber();
        this.expectedPeriod = modifiedPostRequest.getExpectedPeriod();
        this.postStatus = PostStatus.valueOf(modifiedPostRequest.getPostStatus());
    }

    public List<ProfileResponse> toPostProfileResponse() {
        return profiles.stream()
                .filter(postImage -> postImage.getFile() != null)
                .map(postImage -> postImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public void validateLikeCount() {
        if (this.likes <= 0) {
            throw new IllegalStateException("cannot cancel like");
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title=" + title +
                ", contents=" + contents +
                ", user=" + user +
                ", hits=" + hits +
                ", likes=" + likes +
                ", personnelNumber=" + personnelNumber +
                ", comments=" + comments +
                ", expectedPeriod=" + expectedPeriod +
                ", category=" + category +
                ", profiles=" + profiles +
                ", isDeleted=" + isDeleted +
                ", isLike=" + isLike +
                ", postStatus=" + postStatus +
                '}';
    }
}
