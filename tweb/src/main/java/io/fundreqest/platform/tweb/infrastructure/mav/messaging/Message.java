package io.fundreqest.platform.tweb.infrastructure.mav.messaging;

public class Message {
    private MessageType messageType;
    private String code;
    private Object[] args;

    public Message(MessageType messageType, String code, Object[] args) {
        this.messageType = messageType;
        this.code = code;
        this.args = args;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }
}
