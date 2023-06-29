package com.assemble.user.entity;

import com.assemble.file.entity.AttachedFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private AttachedFile file;

    public UserImage(User user, AttachedFile file) {
        this (null, user, file);
    }

    protected UserImage() {
    }
}
