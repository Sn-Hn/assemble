package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class UnauthorizedException extends AssembleException {
    static final String MESSAGE = "error.authorization";

    static final String DETAIL_MESSAGE = "error.authorization.detail";

    public UnauthorizedException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public UnauthorizedException(String message, String messageDetail) {
        super(message, messageDetail, null);
    }

    public UnauthorizedException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public UnauthorizedException(String targetName, Object... values) {
        super(MESSAGE, DETAIL_MESSAGE, new String[]{
                targetName,
                (values != null && values.length > 0) ? StringUtils.arrayToDelimitedString(values, ", ") : ""
        });
    }

    @Override
    public String getMessage() {
        return MessageUtils.getMessage(getDetailMessage(), getParams());
    }
}