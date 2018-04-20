package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import io.fundreqest.platform.tweb.infrastructure.mav.messaging.MessageType;
import io.fundreqest.platform.tweb.infrastructure.mav.messaging.Messages;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class MessageHolder {
    private static final String ATTRIBUTE_NAME = "messages";
    private Messages messages;
    private MessageSource messageSource;
    private Locale locale;

    public MessageHolder(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = LocaleContextHolder.getLocale();
    }

    public String getMessagesAttributeName() {
        return ATTRIBUTE_NAME;
    }

    private void addMessage(MessageType messageType, String code) {
        getMessages().add(messageType, code);
    }

    private void addMessage(MessageType messageType, String code, Object[] params) {
        getMessages().add(messageType, code, params);
    }

    public void addSuccessMessage(String code) {
        addMessage(MessageType.SUCCESS, code);
    }

    public void addErrorMessage(String code) {
        addMessage(MessageType.ERROR, code);
    }

    public void addAlertMessage(String code) {
        addMessage(MessageType.ALERT, code);
    }

    public void addInfoMessage(String code) {
        addMessage(MessageType.INFO, code);
    }

    public void addSuccessMessage(String code, String... args) {
        addMessage(MessageType.SUCCESS, code, args);
    }

    public void addErrorMessage(String code, String... args) {
        addMessage(MessageType.ERROR, code, args);
    }

    public void addAlertMessage(String code, String... args) {
        addMessage(MessageType.ALERT, code, args);
    }

    public void addInfoMessage(String code, String... args) {
        addMessage(MessageType.INFO, code, args);
    }

    public void addMessages(Messages messages) {
        getMessages().addExistingMessages(messages);
    }

    public Messages getMessages() {
        if (messages == null) {
            messages = new Messages(messageSource, locale);
        }
        return messages;
    }
}
