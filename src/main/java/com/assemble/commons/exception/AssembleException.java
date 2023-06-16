package com.assemble.commons.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssembleException extends RuntimeException {

    private final String message;

    private final String detailMessage;

    private final Object[] params;

    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public Object[] getParams() {
        return params;
    }
}
