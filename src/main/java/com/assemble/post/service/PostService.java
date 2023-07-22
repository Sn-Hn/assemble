package com.assemble.post.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeService postLikeService;

    @Transactional(rollbackFor = AssembleException.class)
    public PostCreationResponse createPost(PostCreationRequest postCreationRequest) {
        Post post = postCreationRequest.toEntity();
        post.create(post.getUser().getUserId());

        Post savedPost = postRepository.save(post);
        return new PostCreationResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPosts(PostSearchRequest postSearchRequest, Pageable pageable) {
        Page<Post> posts = postRepository.findAllBySearch(postSearchRequest, pageable);

        return posts;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        PostLikeRequest postLikeRequest = new PostLikeRequest(postId, userId);
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
}
