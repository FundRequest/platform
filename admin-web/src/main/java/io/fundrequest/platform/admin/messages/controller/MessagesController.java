package io.fundrequest.platform.admin.messages.controller;

import io.fundrequest.platform.profile.message.MessageService;
import io.fundrequest.platform.profile.message.domain.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return new ModelAndView("messages");
    }
}
