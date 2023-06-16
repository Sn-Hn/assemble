package com.assemble.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String getMessage(String message) {
        return getMessage(message, null);
    }

    public static String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }
}
