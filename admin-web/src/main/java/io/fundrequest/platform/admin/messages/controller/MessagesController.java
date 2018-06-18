package io.fundrequest.platform.admin.messages.controller;

import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessagesController {

    private MessageService messageService;

    public MessagesController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public ModelAndView showMessagesPage(final Model model) {
        model.addAttribute("referralMessages", messageService.getMessagesByType(MessageType.REFERRAL_SHARE));
        return new ModelAndView("messages/index");
    }

    @RequestMapping("/messages/{type}")
    public ModelAndView showMessagesPage(final Model model, @PathVariable String type) {
        model.addAttribute("messages", messageService.getMessagesByType(MessageType.valueOf(type.toUpperCase())));
        return new ModelAndView("messages/type");
    }

    @RequestMapping("/messages/edit/{type}/{name}")
    public ModelAndView showMessagesPage(final Model model, @PathVariable String type, @PathVariable String name) {
        model.addAttribute("message", messageService.getMessageByNameAndType(name, MessageType.valueOf(type.toUpperCase())));

        return new ModelAndView("messages/edit");
    }

    @PostMapping("/messages/edit")
    public String update(WebRequest request, final Model model) {
        MessageDto messageDto = MessageDto.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .name(request.getParameter("name"))
                .title(request.getParameter("title"))
                .description(request.getParameter("description"))
                .link(request.getParameter("link"))
                .type(MessageType.valueOf(request.getParameter("type"))).build();
        messageService.update(messageDto);
        model.addAttribute("message", messageDto);
        model.addAttribute("success", true);
        return "messages/edit/";
    }
}
