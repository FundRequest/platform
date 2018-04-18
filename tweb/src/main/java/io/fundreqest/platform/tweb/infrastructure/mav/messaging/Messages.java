package io.fundreqest.platform.tweb.infrastructure.mav.messaging;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Messages {

    private MessageSource messageSource;
    private Locale locale;

    private ArrayList<String> infoMessages = new ArrayList<>();
    private ArrayList<String> successMessages = new ArrayList<>();
    private ArrayList<String> errorMessages = new ArrayList<>();
    private ArrayList<String> alertMessages = new ArrayList<>();

    public Messages() {
    }

    public Messages(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public void add(MessageType messageType, String code) {
        add(messageType, code, null);
    }

    public void add(MessageType messageType, String code, Object[] args) {
        String message;

        if (messageSource == null || locale == null) {
            message = markCodeAsNotFound(code);
        } else {
            try {
                message = messageSource.getMessage(code, args, locale);
            } catch (NoSuchMessageException nsme) {
                message = markCodeAsNotFound(code);
            }
        }

        addMessageToList(messageType, message);
    }

    public void addExistingMessages(Messages messages) {
        addMessagesToList(MessageType.ERROR, messages.getErrorMessages());
        addMessagesToList(MessageType.INFO, messages.getInfoMessages());
        addMessagesToList(MessageType.SUCCESS, messages.getSuccessMessages());
        addMessagesToList(MessageType.ALERT, messages.getAlertMessages());
    }

    private void addMessagesToList(MessageType messageType, List<String> messages) {
        for (String message : messages) {
            addMessageToList(messageType, message);
        }
    }

    private void addMessageToList(MessageType messageType, String message) {
        switch (messageType) {
            case ERROR:
                errorMessages.add(message);
                break;
            case INFO:
                infoMessages.add(message);
                break;
            case SUCCESS:
                successMessages.add(message);
                break;
            case ALERT:
                alertMessages.add(message);
                break;
        }
    }

    public void addAsString(MessageType messageType, String message) {
        addMessageToList(messageType, message);
    }

    private String markCodeAsNotFound(String code) {
        return "???" + code + "???";
    }

    public ArrayList<String> getInfoMessages() {
        return infoMessages;
    }

    public ArrayList<String> getSuccessMessages() {
        return successMessages;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public ArrayList<String> getAlertMessages() {
        return alertMessages;
    }
}
