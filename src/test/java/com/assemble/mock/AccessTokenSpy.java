package com.assemble.mock;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

public class AccessTokenSpy {

    private static JwtProvider jwtProvider;

    public AccessTokenSpy(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public static String generateAccessToken() {
        User user = UserFixture.회원();
        return jwtProvider.createAccessToken(user.getUserId(), user.getEmail().getValue());
    }
}
