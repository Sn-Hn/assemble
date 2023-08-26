package com.assemble.commons.base;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContext {
    // default -> 로그인하지 않은 회원
    private Long userId = -1L;

    private String email;

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}
