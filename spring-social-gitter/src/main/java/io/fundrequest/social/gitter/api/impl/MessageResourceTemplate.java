package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Message;
import io.fundrequest.social.gitter.api.MessageResource;
import org.springframework.web.client.RestTemplate;

public class MessageResourceTemplate implements MessageResource {

    private RestTemplate restTemplate;

    public MessageResourceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendMessage(final String roomId, final String text) {
        restTemplate.postForEntity("https://api.gitter.im/v1/rooms/{roomId}/chatMessages", new Message(text), Object.class, roomId);
    }
}
