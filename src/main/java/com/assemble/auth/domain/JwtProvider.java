package com.assemble.auth.domain;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long accessTokenExpireTime = Duration.ofMinutes(30).toMillis();

    private final long refreshTokenExpireTime = Duration.ofDays(14).toMillis();

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(Long userId, String email){

        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("email", email);
        claims.put("typ", "accessToken");

        return createToken(claims, accessTokenExpireTime);
    }

    public String createRefreshToken(Long userId, String email){

        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("email", email);
        claims.put("typ", "refreshToken");

        return createToken(claims, refreshTokenExpireTime);
    }

    private String createToken(Claims claims, long expireTime) {
        Date now = new Date();

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email").toString();
    }

    public Date getTokenCreateDate(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getIssuedAt();
    }

    public Date getTokenExpiraionDate(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }

    public Object getTokenProperty(String token, String key) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(key);
    }

    public boolean isValidToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException: {}", "Invalid jwt signature", e);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: {}","Expired token", e);
        } catch (UnsupportedJwtException e) {
            log.error("UnsupportedJwtException: {}", "Unsupported token", e);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException: {}", "Invalid token", e);
        } catch (SignatureException e) {
            log.error("SignatureException: {}", "Signature error", e);
        } catch (Exception e) {
            log.error("Exception: {}", "Unknown error", e);
        }

        return false;
    }

}
