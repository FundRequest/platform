package io.fundrequest.platform.profile.message;

import io.fundrequest.platform.profile.message.domain.MessageType;
import io.fundrequest.platform.profile.message.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessagesByType(MessageType type);

    MessageDto getMessageByKey(String key);

    MessageDto getMessageByNameAndType(String name, MessageType type);
}
