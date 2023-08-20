package com.assemble.post.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeService postLikeService;

    private final UserContext userContext;

    @Transactional
    public Post createPost(PostCreationRequest postCreationRequest) {
        Post post = postCreationRequest.toEntity(userContext.getUserId());
        post.createUser(post.getUser().getUserId());

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPosts(PostSearchRequest postSearchRequest, Pageable pageable) {
        long count = postRepository.countBySearch(postSearchRequest);
        List<Post> posts = postRepository.findAllBySearch(postSearchRequest, userContext.getUserId(), pageable);

        return new PageImpl<>(posts, pageable, count);
    }

    @Transactional
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        // TODO: 2023-07-22 리팩터링 필요 (조회수 계속 올라감) -신한
        postRepository.increaseHits(postId);

        PostLikeRequest postLikeRequest = new PostLikeRequest(postId);
        post.setIsLike(postLikeService.isAleadyLikeByUser(postLikeRequest));

        return post;
    }

    @Transactional
    public Post modifyPost(ModifiedPostRequest modifiedPostRequest) {
        Post post = getPostOfWriter(modifiedPostRequest.getPostId());

        post.modifyPost(modifiedPostRequest);

        return post;
    }

    @Transactional
    public boolean deletePost(Long postId) {
        Post post = getPostOfWriter(postId);

        postRepository.delete(post);

        return true;
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostsByUser(Long userId, Pageable pageable) {
        long count = postRepository.countByUserId(userId);
        List<Post> posts = postRepository.findAllByUserId(userId, userContext.getUserId(), pageable);

        return new PageImpl<>(posts, pageable, count);
    }

    private Post getPostOfWriter(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        if (!userContext.getUserId().equals(post.getUser().getUserId())) {
            throw new IllegalArgumentException("not writer");
        }

        return post;
    }
}
