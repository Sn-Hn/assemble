//package com.assemble.auth.domain;
//
//import lombok.Getter;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//import org.springframework.data.redis.core.TimeToLive;
//import org.springframework.data.redis.core.index.Indexed;
//
//@Getter
//@RedisHash(value = "accessToken")
//public class AccessToken {
//
//    @Id
//    private String key;
//
//    @Indexed
//    private String token;
//
//    @TimeToLive
//    private int expiredTime;
//
//    public AccessToken(String key, String token, int expiredTime) {
//        this.key = key;
//        this.token = token;
//        this.expiredTime = expiredTime;
//    }
//}
