package com.assemble.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") List<String> baseNames,
            @Value("${spring.messages.encoding}") String encoding
    ) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        baseNames.forEach(messageSource::addBasenames);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
