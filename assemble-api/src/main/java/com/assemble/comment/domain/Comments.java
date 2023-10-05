package com.assemble.comment.domain;

import com.assemble.comment.entity.Comment;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Comments {

    @OneToMany(mappedBy = "meeting")
    private List<Comment> comments = new ArrayList<>();

    public void add(Comment comment) {
        this.comments.add(comment);
    }
}
