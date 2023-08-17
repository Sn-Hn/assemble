package com.assemble.commons.exception;

import com.assemble.util.MessageUtils;
import org.springframework.util.StringUtils;

public class FileUploadException extends AssembleException {

    static final String MESSAGE = "error.file.upload";

    static final String DETAIL_MESSAGE = "error.file.upload.detail";

    public FileUploadException() {
        super(MESSAGE, DETAIL_MESSAGE, null);
    }

    public FileUploadException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public FileUploadException(String targetName, Object... values) {
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
