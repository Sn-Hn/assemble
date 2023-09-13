package com.assemble.util;

import com.assemble.annotation.TestCustomProperty;
import com.assemble.auth.domain.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestCustomProperty
@DisplayName("JwtUtils")
public class JwtUtilsTest {

    @Autowired
    private JwtProvider jwtProvider;

}
