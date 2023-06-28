package com.assemble.comment.domain;

import com.assemble.comment.entity.Comment;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Comments {

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public Comments() {
        this.comments = new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void add(Comment comment) {
        this.comments.add(comment);
    }
}
