package com.assemble.commons.response;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

public class ApiError {

    @ApiModelProperty(value = "오류 메시지", required = true)
    private String message;

    private int status;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ApiError(String message, HttpStatus status) {
        this(message, status.value());
    }

    public ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status);
    }

    private ApiError() {}

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "message='" + message + '\'' +
                '}';
    }
}
