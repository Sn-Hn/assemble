package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class UnauthenticationException extends AssembleException {
    static final String MESSAGE = "error.authentication";

    static final String DETAIL_MESSAGE = "error.authentication.detail";

    public UnauthenticationException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public UnauthenticationException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public UnauthenticationException(String targetName, Object... values) {
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
