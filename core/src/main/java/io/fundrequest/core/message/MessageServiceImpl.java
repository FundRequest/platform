package io.fundrequest.core.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.message.domain.Message;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import io.fundrequest.core.message.infrastructure.MessageRepository;
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
            return getMessageByTypeAndName(MessageType.valueOf(type.toUpperCase()), name);
        } else {
            return null;
        }
    }

    @Override
    public MessageDto getMessageByTypeAndName(MessageType type, String name) {
        Message m = repository.findByTypeAndName(type, name).orElseThrow(() -> new RuntimeException("Message not found"));
        return createMessageDto(m);
    }

    @Transactional
    @Override
    public Message update(MessageDto messageDto) {
        Message m = repository.findByTypeAndName(messageDto.getType(), messageDto.getName()).orElseThrow(() -> new RuntimeException("Message not found"));
        messageDto.setId(m.getId());
        Message newM = objectMapper.convertValue(messageDto, Message.class);

        return repository.save(newM);
    }

    @Transactional
    @Override
    public Message add(MessageDto messageDto) {
        int minlength = 3;
        if (repository.findByTypeAndName(messageDto.getType(), messageDto.getName()).isPresent()) {
            new RuntimeException(String.format("Message with type %s and name %s already exists", messageDto.getType().toString(), messageDto.getName()));
        }
        if (messageDto.getName().length() < 3) {
            new RuntimeException(String.format("Message name should be at least %s characters long", minlength));
        }

        Message newM = objectMapper.convertValue(messageDto, Message.class);

        return repository.save(newM);
    }

    @Transactional
    @Override
    public void delete(MessageType type, String name) {
        repository.deleteByTypeAndName(type, name);
    }

    private MessageDto createMessageDto(Message m) {
        return MessageDto.builder()
                .id(m.getId())
                .type(m.getType())
                .name(m.getName())
                .title(m.getTitle())
                .description(m.getDescription())
                .link(m.getLink())
                .build();
    }
}
