package io.fundrequest.platform.profile.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.profile.message.domain.Message;
import io.fundrequest.platform.profile.message.domain.MessageType;
import io.fundrequest.platform.profile.message.dto.MessageDto;
import io.fundrequest.platform.profile.message.infrastructure.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
class MessageServiceImpl implements MessageService {

    private final ObjectMapper objectMapper;
    private MessageRepository repository;

    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageDto> getMessagesByType(MessageType type) {
        return repository.findByType(type, new Sort(Sort.Direction.DESC, "name"))
                .stream()
                .parallel()
                .map(this::createMessageDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto getMessageByKey(String key) {
        int indexSeperator = key.indexOf('.');

        if (indexSeperator > 0) {
            String type = key.substring(0, indexSeperator);
            String name = key.substring(indexSeperator + 1);
            return getMessageByNameAndType(name, MessageType.valueOf(type.toUpperCase()));
        } else {
            return null;
        }
    }

    @Override
    public MessageDto getMessageByNameAndType(String name, MessageType type) {
        Message m = repository.findByNameAndType(name, type);
        if (m != null) {
            return createMessageDto(repository.findByNameAndType(name, type));
        } else {
            return null;
        }
    }


    private MessageDto createMessageDto(Message m) {
        return MessageDto.builder()
                .type(m.getType())
                .name(m.getName())
                .title(m.getTitle())
                .description(m.getDescription())
                .link(m.getLink())
                .build();
    }
}
