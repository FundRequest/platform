package io.fundrequest.core.message;

import io.fundrequest.core.message.domain.Message;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessagesByType(MessageType type);

    MessageDto getMessageByKey(String key);

    MessageDto getMessageByTypeAndName(MessageType type, String name);

    Message update(MessageDto message);

    Message add(MessageDto message);

    void delete(MessageType type, String name);
}
