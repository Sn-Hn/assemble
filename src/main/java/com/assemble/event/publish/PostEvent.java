package com.assemble.event.publish;

import com.assemble.post.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostEvent {

    private final Post post;

}
