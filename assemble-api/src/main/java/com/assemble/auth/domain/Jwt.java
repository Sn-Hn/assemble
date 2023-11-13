package com.assemble.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "RefreshToken", timeToLive = 60 * 60 * 24 * 14)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jwt {

    @Id
    private String key;

    private String refreshToken;

    public Jwt(String refreshTokenKey, String refreshToken) {
        this.key = refreshTokenKey;
        this.refreshToken = refreshToken;
    }
}
