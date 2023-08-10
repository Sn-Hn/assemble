package com.assemble.post.service;

import com.assemble.commons.base.BaseRequest;
import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeService postLikeService;

    private final BaseRequest baseRequest;

    @Transactional(rollbackFor = AssembleException.class)
    public Post createPost(PostCreationRequest postCreationRequest) {
        Post post = postCreationRequest.toEntity(baseRequest.getUserId());
        post.createUser(post.getUser().getUserId());

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPosts(PostSearchRequest postSearchRequest, Pageable pageable) {
        long count = postRepository.countBySearch(postSearchRequest);
        Page<Post> posts = postRepository.findAllBySearch(postSearchRequest, baseRequest.getUserId(), pageable, count);

        return posts;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        PostLikeRequest postLikeRequest = new PostLikeRequest(postId);
        post.setIsLike(postLikeService.isAleadyLikeByUser(postLikeRequest));

        // TODO: 2023-07-22 리팩터링 필요 (조회수 계속 올라감) -신한
        post.increaseHits();

        return post;
    }

    @Transactional(rollbackFor = AssembleException.class)
    public Post modifyPost(ModifiedPostRequest modifiedPostRequest) {
        Post post = postRepository.findById(modifiedPostRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(Post.class, modifiedPostRequest.getPostId()));

        post.modifyPost(modifiedPostRequest);

        return post;
    }

    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        postRepository.delete(post);

        return true;
    }

    public Page<Post> getPostsByUser(Long userId, Pageable pageable) {
        long count = postRepository.countByUserId(userId);
        Page<Post> posts = postRepository.findAllByUserId(userId, baseRequest.getUserId(), pageable, count);

        return posts;
    }
}
