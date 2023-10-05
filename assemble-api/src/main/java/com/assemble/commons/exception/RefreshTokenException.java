package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class RefreshTokenException extends AssembleException {
    static final String MESSAGE = "error.refresh.token";

    static final String DETAIL_MESSAGE = "error.refresh.token.detail";

    public RefreshTokenException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public RefreshTokenException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public RefreshTokenException(String targetName, Object... values) {
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
