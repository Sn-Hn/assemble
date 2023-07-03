package com.assemble.post.domain;

import com.assemble.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Posts {

    List<Post> posts = new ArrayList<>();
}
