package com.assemble.user.entity;

import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.file.entity.AttachedFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_image SET is_deleted = 'Y' WHERE user_image_id = ?")
@Where(clause = "is_deleted = 'N'")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fileId")
    private AttachedFile file;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    public UserImage(User user, AttachedFile file) {
        this (null, user, file, false);
    }

    protected UserImage() {
    }
}
