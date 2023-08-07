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
    public boolean likePost(PostLikeRequest postLikeRequest) {
        Likes postLike = postLikeRequest.toEntity();
        if (isAleadyLikeByUser(postLikeRequest)) {
            throw new IllegalArgumentException("this post is already like");
        }

        postLikeRepository.save(postLike);

        Post post = postRepository.findById(postLikeRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(Post.class, postLikeRequest.getPostId()));

        post.increaseLikes();

        return true;
    }

    @Transactional(rollbackFor = AssembleException.class)
    public boolean cancelLikePost(Long postId) {
        Likes likes = postLikeRepository.findPostByUser(postId)
                .orElseThrow(() -> new IllegalArgumentException("this post didn't like it"));

        postLikeRepository.delete(likes);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        post.decreaseLikes();

        return true;
    }

    @Transactional(readOnly = true)
    public boolean isAleadyLikeByUser(PostLikeRequest postLikeRequest) {
        if (postLikeRepository.findPostByUser(postLikeRequest.getPostId()).isPresent()) {
            return true;
        }

        return false;
    }
}
