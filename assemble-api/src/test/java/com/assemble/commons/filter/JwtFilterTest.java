package com.assemble.commons.filter;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.TokenFixture;
import com.assemble.commons.exclusion.ExclusionApis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private ExclusionApis exclusionApis;

    @ParameterizedTest
    @CsvSource(value = {"/bypass-success:true", "/bypass-fail:false"}, delimiter = ':')
    void JWT_검증_필터_BYPASS_검증(String servletPath, boolean expected) throws ServletException {
        // given
        HashMap<String, String> apis = new HashMap<>();
        apis.put("/bypass-success", "GET");
        given(exclusionApis.getExclusionApis()).willReturn(apis);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", servletPath);
        mockRequest.setServletPath(servletPath);

        // when
        boolean isBypass = jwtFilter.shouldNotFilter(mockRequest);

        // then
        assertThat(isBypass).isEqualTo(expected);
    }

    @Test
    void JWT_검증() {
        // given
        String mockAccessToken = TokenFixture.AccessToken_생성();
        given(jwtProvider.isValidToken(anyString())).willReturn(true);
        MockFilterChain mockFilterChain = new MockFilterChain();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/test");
        mockRequest.setServletPath("/test");
        mockRequest.addHeader("Authorization", mockAccessToken);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        // when, then
        assertThatCode(() -> jwtFilter.doFilterInternal(mockRequest, mockHttpServletResponse, mockFilterChain)).doesNotThrowAnyException();
    }
}
