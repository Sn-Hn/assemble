package com.assemble.util;

import com.assemble.annotation.TestCustomProperty;
import com.assemble.auth.domain.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestCustomProperty
@DisplayName("JwtUtils")
public class JwtUtilsTest {

    @Autowired
    private JwtProvider jwtProvider;

}
