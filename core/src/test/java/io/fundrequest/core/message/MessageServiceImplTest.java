package io.fundrequest.core.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.message.domain.Message;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import io.fundrequest.core.message.infrastructure.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageServiceImplTest {

    private ObjectMapper objectMapper;
    private MessageService messageService;
    private MessageRepository messageRepository;

    private Message message1;
    private MessageDto messageDto1;

    @BeforeEach
    void setUp() {
        message1 = Message.builder().type(MessageType.REFERRAL_SHARE).name("message1").build();
        messageDto1 = MessageDto.builder().type(MessageType.REFERRAL_SHARE).name("message1").build();

        objectMapper = mock(ObjectMapper.class);
        messageRepository = mock(MessageRepository.class);
        messageService = new MessageServiceImpl(messageRepository, objectMapper);

        when(objectMapper.convertValue(message1, MessageDto.class)).thenReturn(messageDto1);
        when(messageRepository.findByTypeAndName(eq(MessageType.REFERRAL_SHARE), argThat(anyOf(equalTo("message1"), equalTo("m1")))))
                .thenReturn(Optional.of(message1));
        when(messageRepository.findByTypeAndName(any(MessageType.class), argThat(not(anyOf(equalTo("message1"), equalTo("m1"))))))
                .thenReturn(Optional.empty());
    }

    @Test
    void getMessagesByType() {
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        MessageDto messageDto2 = mock(MessageDto.class);
        MessageDto messageDto3 = mock(MessageDto.class);
        when(objectMapper.convertValue(same(message2), eq(MessageDto.class))).thenReturn(messageDto2);
        when(objectMapper.convertValue(same(message3), eq(MessageDto.class))).thenReturn(messageDto3);

        when(messageRepository.findByType(MessageType.REFERRAL_SHARE, new Sort(Sort.Direction.DESC, "name")))
                .thenReturn(Arrays.asList(message1, message2, message3));

        List<MessageDto> result = messageService.getMessagesByType(MessageType.REFERRAL_SHARE);

        assertThat(result).containsExactly(messageDto1, messageDto2, messageDto3);
    }

    @Test
    void getMessageByKey() {
        MessageDto result = messageService.getMessageByKey("REFERRAL_SHARE.message1");
        assertThat(result).isEqualTo(messageDto1);
    }

    @Test
    void getMessageByKey_invalidKey() {
        MessageDto result = messageService.getMessageByKey("INVALIDKEY");
        assertThat(result).isNull();
    }

    @Test
    void getMessageByTypeAndName() {
        MessageDto result = messageService.getMessageByTypeAndName(MessageType.REFERRAL_SHARE, "message1");
        assertThat(result).isEqualTo(messageDto1);
    }

    @Test
    void getMessageByTypeAndName_throwsExceptionWhenNotFound() {
        when(messageRepository.findByTypeAndName(MessageType.REFERRAL_SHARE, "doesnotexist"))
                .thenReturn(Optional.empty());

        try {
            messageService.getMessageByTypeAndName(MessageType.REFERRAL_SHARE, "doesnotexist");
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Message not found");
        }
    }

    @Test
    void update() {
        when(objectMapper.convertValue(messageDto1, Message.class)).thenReturn(message1);
        when(messageRepository.save(message1)).thenReturn(message1);

        assertThat(messageService.update(messageDto1)).isEqualTo(message1);
    }

    @Test
    void update_throwsExceptionWhenNotExists() {
        MessageDto messageDto = mock(MessageDto.class);

        try {
            messageService.update(messageDto);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Message not found");
        }
    }

    @Test
    void add() {
        Message message = Message.builder().type(MessageType.REFERRAL_SHARE).name("myNewItem").build();
        MessageDto messageDto = MessageDto.builder().type(MessageType.REFERRAL_SHARE).name("myNewItem").build();

        when(objectMapper.convertValue(messageDto, Message.class)).thenReturn(message);
        when(messageRepository.save(message)).thenReturn(message);

        Message result = messageService.add(messageDto);
        assertThat(result).isEqualTo(message);
    }

    @Test
    void add_throwsExceptionWhenExists() {
        try {
            messageService.add(messageDto1);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo(String.format("Message with type %s and name %s already exists", messageDto1.getType().toString(), messageDto1.getName()));
        }
    }

    @Test
    void add_throwsExceptionWhenNameIsToShort() {
        MessageDto messageDto = MessageDto.builder().type(MessageType.REFERRAL_SHARE).name("m1").build();

        try {
            messageService.add(messageDto);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Message name should be at least 3 characters long");
        }
    }

    @Test
    void delete() {
        messageService.delete(message1.getType(), message1.getName());
        verify(messageRepository).deleteByTypeAndName(message1.getType(), message1.getName());
    }
}
