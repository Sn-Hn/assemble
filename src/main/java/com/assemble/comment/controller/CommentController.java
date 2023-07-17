package com.assemble.comment.controller;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.dto.response.CommentResponse;
import com.assemble.comment.service.CommentService;
import com.assemble.commons.response.ApiResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "댓글 APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResult<CommentResponse> createCommnet(CommentCreationRequest commentCreationRequest) {
        return ApiResult.ok(new CommentResponse(commentService.createComment(commentCreationRequest)));
    }

    @PatchMapping
    public ApiResult<CommentResponse> modifyComment(ModifiedCommentRequest modifiedCommentRequest) {
        return ApiResult.ok(new CommentResponse(commentService.modifyComment(modifiedCommentRequest)));
    }

    @DeleteMapping(path = "{commentId}")
    public ApiResult<Boolean> deleteComment(@PathVariable Long commentId) {
        return ApiResult.ok(commentService.deleteComment(commentId));
    }
}
