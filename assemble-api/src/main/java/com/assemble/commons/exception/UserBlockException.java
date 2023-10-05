package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class UserBlockException extends AssembleException {
    static final String MESSAGE = "error.user.block";

    static final String DETAIL_MESSAGE = "error.user.block.detail";

    public UserBlockException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public UserBlockException(String message, String messageDetail) {
        super(message, messageDetail, null);
    }

    public UserBlockException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public UserBlockException(String targetName, Object... values) {
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
