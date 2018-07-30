package io.fundrequest.platform.admin.messages.controller;

import io.fundrequest.common.infrastructure.AbstractControllerTest;
import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class MessagesControllerTest extends AbstractControllerTest<MessagesController> {
    private MessageService messageService;

    @Override
    protected MessagesController setupController() {
        messageService = mock(MessageService.class);
        return new MessagesController(messageService);
    }

    @Test
    public void showAllMessagesPage() throws Exception {
        MessageDto messageDto1 = mock(MessageDto.class);
        MessageDto messageDto2 = mock(MessageDto.class);
        MessageDto messageDto3 = mock(MessageDto.class);
        List<MessageDto> messages = Arrays.asList(messageDto1, messageDto2, messageDto3);

        when(messageService.getMessagesByType(MessageType.REFERRAL_SHARE)).thenReturn(messages);

        this.mockMvc.perform(get("/messages"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("referralMessages", messages))
                .andExpect(MockMvcResultMatchers.view().name("messages/index"));
    }

    @Test
    public void showMessagesOfTypePage() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();
        MessageDto messageDto1 = mock(MessageDto.class);
        MessageDto messageDto2 = mock(MessageDto.class);
        MessageDto messageDto3 = mock(MessageDto.class);
        List<MessageDto> messages = Arrays.asList(messageDto1, messageDto2, messageDto3);

        when(messageService.getMessagesByType(MessageType.REFERRAL_SHARE)).thenReturn(messages);

        this.mockMvc.perform(get("/messages/{type}", type))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("type", type))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", messages))
                .andExpect(MockMvcResultMatchers.view().name("messages/type"));
    }

    @Test
    public void showAddMessageToTypePage() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();

        this.mockMvc.perform(get("/messages/{type}/add", type))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("message", MessageDto.builder().type(MessageType.valueOf(type)).build()))
                .andExpect(MockMvcResultMatchers.view().name("messages/add"));
    }

    @Test
    public void showEditPage() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();
        String name = "name";
        MessageDto messageDto = mock(MessageDto.class);

        when(messageService.getMessageByTypeAndName(MessageType.REFERRAL_SHARE, name)).thenReturn(messageDto);

        this.mockMvc.perform(get("/messages/{type}/{name}/edit", type, name))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("message", messageDto))
                .andExpect(MockMvcResultMatchers.view().name("messages/edit"));
    }

    @Test
    public void add() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();

        this.mockMvc.perform(post("/messages/{type}/add", type))
                .andExpect(redirectAlert("success", "Message added"))
                .andExpect(redirectedUrl("/messages/" + type));
    }

    @Test
    public void update() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();
        String name = "name";

        this.mockMvc.perform(post("/messages/{type}/{name}/edit", type, name))
                .andExpect(redirectAlert("success", "Saved"))
                .andExpect(redirectedUrl("/messages/" + type));
    }

    @Test
    public void delete() throws Exception {
        String type = MessageType.REFERRAL_SHARE.toString();
        String name = "name";

        this.mockMvc.perform(post("/messages/{type}/{name}/delete", type, name))
                .andExpect(redirectAlert("success", "Deleted"))
                .andExpect(redirectedUrl("/messages/" + type));
    }
}