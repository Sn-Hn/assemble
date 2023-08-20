package com.assemble.post.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.entity.Likes;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostLikeRepository;
import com.assemble.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserContext userContext;

    @Transactional
    public boolean likePost(PostLikeRequest postLikeRequest) {
        Likes postLike = postLikeRequest.toEntity(userContext.getUserId());
        if (isAleadyLikeByUser(postLikeRequest)) {
            throw new IllegalArgumentException("this post is already like");
        }

        postLikeRepository.save(postLike);

        Post post = postRepository.findById(postLikeRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(Post.class, postLikeRequest.getPostId()));

        post.increaseLikes();

        return true;
    }

    @Transactional
    public boolean cancelLikePost(Long postId) {
        Likes likes = postLikeRepository.findPostByUser(postId, userContext.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("this post didn't like it"));

        postLikeRepository.delete(likes);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        post.decreaseLikes();

        return true;
    }

    @Transactional(readOnly = true)
    public boolean isAleadyLikeByUser(PostLikeRequest postLikeRequest) {
        if (postLikeRepository.findPostByUser(postLikeRequest.getPostId(), userContext.getUserId()).isPresent()) {
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostsByLike(Pageable pageable) {
        Long userId = userContext.getUserId();
        long count = postLikeRepository.countByUserId(userId);
        List<Post> posts = postLikeRepository.findAllByUserId(userId, pageable).stream()
                .map(like -> like.getPost())
                .collect(Collectors.toUnmodifiableList());

        return new PageImpl<>(posts, pageable, count);
    }
}
