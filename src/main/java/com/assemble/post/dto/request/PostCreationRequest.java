package com.assemble.post.dto.request;

import com.assemble.category.entity.Category;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "PostCreationRequest : 게시글 작성 요청 값")
@AllArgsConstructor
@Getter
@ToString
public class PostCreationRequest {

    @ApiModelProperty(value = "게시글 제목", required = true)
    @NotEmpty
    private String title;

    @ApiModelProperty(value = "게시글 내용", required = true)
    @NotEmpty
    private String contents;

    @ApiModelProperty(value = "게시글 카테고리", required = true, example = "1")
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "모집 인원", example = "0")
    @NotNull
    private int personnelNumber;

    @ApiModelProperty(value = "예상 기간", example = "0")
    @NotNull
    private int expectedPeriod;

    private PostCreationRequest() {
    }

    public Post toEntity(Long userId) {
        return new Post(
                new Title(this.title),
                new Contents(this.contents),
                new User(userId),
                this.personnelNumber,
                this.expectedPeriod,
                new Category(categoryId)
        );
    }
}
