package com.outsera.goldenraspberryawards.core.internationalization;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public class MessageSystem {

    private static final MessageSystem INSTANCE = new MessageSystem();

    private final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    private MessageSystem() {
        messageSource.setBasename("lang/res");
    }

    public static MessageSystem getInstance() {
        return INSTANCE;
    }

    public String getLocalizedMessage(String messageKey) {
        return getLocalizedMessage(messageKey, null);
    }

    public String getLocalizedMessage(String messageKey, Object[] args) {
        final var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, locale);
    }
}
