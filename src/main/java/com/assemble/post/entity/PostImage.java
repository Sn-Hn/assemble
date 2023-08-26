package com.assemble.post.entity;

import com.assemble.file.entity.AttachedFile;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private AttachedFile file;

    public PostImage(Post post, User user, AttachedFile file) {
        this(null, post, user, file);
    }

    protected PostImage() {
    }
}
