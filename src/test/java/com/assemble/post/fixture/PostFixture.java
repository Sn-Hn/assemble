package com.assemble.post.fixture;

import com.assemble.category.entity.Category;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;

import java.io.IOException;
import java.util.ArrayList;

public class PostFixture {
    private static final Long postId = 1L;
    private static final String title = "스터디 모집";
    private static final String modifiedTitle = "스터디 모집 1명 (수정)";
    private static final String contents = "스터디 모집합니다.";
    private static final String modifiedContents = "스터디 2명 모집합니다. (수정)";
    private static final Long categoryId = 1L;
    private static final String categoryName = "개발/프로그래밍";
    private static final String writerNickname = "개발자";
    private static final Long writer = 1L;
    private static final Long hits = 0L;
    private static final Long likeCount = 0L;
    private static final int personnelNumber = 0;
    private static final int expectedPeriod = 0;
    private static final String searchByTitle = "title";
    private static final String searchQueryTitle = "제목";
    private static final String searchByContents = "contents";
    private static final String searchQueryContents = "내용";

    public static PostCreationRequest 게시글_작성_사진_X() {
        return new PostCreationRequest(
                title,
                contents,
                categoryId,
                writer,
                personnelNumber,
                expectedPeriod
        );
    }

    public static PostCreationRequest 게시글_작성_사진_O() throws IOException {
        return new PostCreationRequest(
                title,
                contents,
                categoryId,
                writer,
                personnelNumber,
                expectedPeriod
        );
    }

    public static PostCreationResponse 게시글_작성_응답() {
        return new PostCreationResponse(title, contents, categoryId, writerNickname, writer, hits, likeCount, new ArrayList<>());
    }

    public static Post 게시글() {
        return new Post(
                postId,
                new Title(title),
                new Contents(contents),
                new User(writer),
                hits,
                likeCount,
                personnelNumber,
                null,
                expectedPeriod,
                new Category(categoryId),
                new ArrayList<>(),
                false
        );
    }

    public static PostSearchRequest 게시글_목록_제목_검색() {
        return new PostSearchRequest(searchQueryTitle, searchByTitle);
    }

    public static PostSearchRequest 게시글_목록_내용_검색() {
        return new PostSearchRequest(searchQueryContents, searchByContents);
    }

    public static ModifiedPostRequest 게시글_수정() {
        return new ModifiedPostRequest(
                postId,
                modifiedTitle,
                modifiedContents,
                categoryId,
                personnelNumber,
                expectedPeriod
        );
    }
}
