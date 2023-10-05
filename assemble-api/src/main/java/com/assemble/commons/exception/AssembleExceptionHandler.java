package com.assemble.commons.exception;

import com.assemble.auth.controller.AuthController;
import com.assemble.auth.domain.JwtType;
import com.assemble.commons.response.ApiResult;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AssembleExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        return newResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthenticationException.class, UnauthorizedException.class, ExpiredJwtException.class,
                    MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class})
    public ResponseEntity<?> handleUnauthenticationException(Exception e) {
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<?> handleExpiredRefreshTokenException(Exception e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(HttpHeaders.SET_COOKIE, AuthController.createCookie(JwtType.REFRESH_TOKEN, null, 0).toString());

        return newResponse(e, HttpStatus.FORBIDDEN, headers);
    }

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<?> handleNotAdminException(Exception e) {
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({AssembleException.class, Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return newResponse(throwable, status, headers);
    }

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status, HttpHeaders headers) {
        log.error("error={}", throwable.getMessage(), throwable);
        return new ResponseEntity<>(ApiResult.error(throwable, status), headers, status);
    }
}
