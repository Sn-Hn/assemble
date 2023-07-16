package com.assemble.post.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.service.PostLikeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "게시판 좋아요 APIs")
@RequiredArgsConstructor
@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("post/like")
    public ApiResult<Void> like(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.createPostLike(postLikeRequest);
        return ApiResult.ok();
    }

    @DeleteMapping("post/like")
    public ApiResult<Void> cancelLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.deletePostLike(postLikeRequest);
        return ApiResult.ok();
    }
}
