package com.assemble.commons.response;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

public class ApiResult<T> {

    @ApiModelProperty(value = "API 요청 처리 결과", required = true)
    private boolean success;

    @ApiModelProperty(value = "HTTP 응답 코드")
    private int status;

    @ApiModelProperty(value = "API 요청 성공 시 응답 값")
    private T response;

    @ApiModelProperty(value = "API 요청 실패 시 응답 값")
    private ApiError error;

    public ApiResult(boolean success, int status, T response, ApiError error) {
        this.success = success;
        this.status = status;
        this.response = response;
        this.error = error;
    }

    private ApiResult() {

    }

    public static ApiResult ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T response) {
        return ok(response, HttpStatus.OK);
    }

    public static <T> ApiResult<T> ok(T response, HttpStatus status) {
        return new ApiResult<>(true, status.value(), response, null);
    }

    public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
        return error(throwable.getMessage(), status);
    }

    public static ApiResult<?> error(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, status.value(), null, new ApiError(errorMessage, status));
    }

    public boolean isSuccess() {
        return success;
    }

    public ApiError getError() {
        return error;
    }

    public T getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "success=" + success +
                ", status=" + status +
                ", response=" + response +
                ", error=" + error +
                '}';
    }
}
