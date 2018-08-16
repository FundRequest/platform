package io.fundrequest.social.gitter.api;

public interface MessageResource {
    void sendMessage(String roomId, String text);
}
