package com.assemble.post.service;

import com.assemble.file.entity.AttachedFile;
import com.assemble.file.service.FileService;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final FileService fileService;

    @Transactional
    public PostCreationResponse createPost(PostCreationRequest postCreationRequest, MultipartFile file) {
        Post post = postCreationRequest.toEntity();
        post.create(post.getUser().getUserId());

        AttachedFile attachedFile = fileService.uploadFile(file, post.getUser().getUserId());
        post.setProfile(attachedFile);

        Post savedPost = postRepository.save(post);
        return new PostCreationResponse(savedPost);
    }
}
