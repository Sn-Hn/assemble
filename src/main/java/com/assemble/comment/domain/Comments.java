package com.assemble.comment.domain;

import com.assemble.comment.entity.Comment;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@BatchSize(size = 50)
public class Comments {

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public void add(Comment comment) {
        this.comments.add(comment);
    }
}
