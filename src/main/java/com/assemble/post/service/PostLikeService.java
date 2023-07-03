package com.assemble.post.service;

import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostLikeRepository;
import com.assemble.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional(rollbackFor = AssembleException.class)
    public void createPostLike(PostLikeRequest postLikeRequest) {
        Likes postLike = postLikeRequest.toEntity();
        postLikeRepository.save(postLike);

        Post post = postRepository.findById(postLikeRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(Post.class, postLikeRequest.getPostId()));

        post.increaseLikes();
    }

    @Transactional(rollbackFor = AssembleException.class)
    public void deletePostLike(PostLikeRequest postLikeRequest) {
        Likes postLike = postLikeRequest.toEntity();
        postLikeRepository.delete(postLike);

        Post post = postRepository.findById(postLikeRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(Post.class, postLikeRequest.getPostId()));

        post.decreaseLikes();
    }
}
