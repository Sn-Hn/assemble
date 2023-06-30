package com.assemble.post.fixture;

import com.assemble.category.entity.Category;
import com.assemble.file.fixture.FileFixture;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;

import java.io.IOException;
import java.util.ArrayList;

public class PostFixture {
    private static final String title = "스터디 모집";
    private static final String contents = "스터디 모집합니다.";
    private static final String category = "스터디";
    private static final String writerNickname = "개발자";
    private static final Long writer = 1L;
    private static final Long hits = 0L;
    private static final Long likeCount = 0L;
    private static final int personnelNumber = 0;
    private static final int expectedPeriod = 0;

    public static PostCreationRequest 게시글_작성_사진_X() {
        return new PostCreationRequest(
                title,
                contents,
                category,
                writer,
                personnelNumber,
                expectedPeriod
        );
    }

    public static PostCreationRequest 게시글_작성_사진_O() throws IOException {
        return new PostCreationRequest(
                title,
                contents,
                category,
                writer,
                personnelNumber,
                expectedPeriod
        );
    }

    public static PostCreationResponse 게시글_작성_응답() {
        return new PostCreationResponse(title, contents, category, writerNickname, writer, hits, likeCount, new ArrayList<>());
    }

    public static Post 게시글() {
        return new Post(
                new Title(title),
                new Contents(contents),
                new User(writer),
                personnelNumber,
                expectedPeriod,
                Category.createCategory(category, writer)
        );
    }
}
