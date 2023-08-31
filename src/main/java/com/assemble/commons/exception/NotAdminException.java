package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class NotAdminException extends AssembleException {
    static final String MESSAGE = "error.not.admin";

    static final String DETAIL_MESSAGE = "error.not.admin.detail";

    public NotAdminException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public NotAdminException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public NotAdminException(String targetName, Object... values) {
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
