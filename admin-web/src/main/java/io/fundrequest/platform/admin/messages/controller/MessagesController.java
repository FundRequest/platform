package io.fundrequest.platform.admin.messages.controller;

import io.fundrequest.common.infrastructure.mav.AbstractController;
import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MessagesController extends AbstractController {

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
        model.addAttribute("type", type.toUpperCase());
        model.addAttribute("messages", messageService.getMessagesByType(MessageType.valueOf(type.toUpperCase())));
        return new ModelAndView("messages/type");
    }

    @RequestMapping("/messages/{type}/add")
    public ModelAndView addMessageToType(final Model model, @PathVariable String type) {
        model.addAttribute("message", MessageDto.builder().type(MessageType.valueOf(type)).build());
        return new ModelAndView("messages/add");
    }

    @PostMapping("/messages/{type}/add")
    public ModelAndView update(WebRequest request, @PathVariable String type, RedirectAttributes redirectAttributes) {
        MessageDto messageDto = MessageDto.builder()
                .name(request.getParameter("name"))
                .type(MessageType.valueOf(type.toUpperCase()))
                .title(request.getParameter("title"))
                .description(request.getParameter("description"))
                .link(request.getParameter("link"))
                .build();

        messageService.add(messageDto);

        return redirectView(redirectAttributes)
                .withSuccessMessage("Message added")
                .url("/messages/" + type)
                .build();
    }

    @RequestMapping("/messages/{type}/{name}/edit")
    public ModelAndView showMessagesPage(final Model model, @PathVariable String type, @PathVariable String name) {
        model.addAttribute("message", messageService.getMessageByTypeAndName(MessageType.valueOf(type.toUpperCase()), name));

        return new ModelAndView("messages/edit");
    }

    @PostMapping("/messages/{type}/{name}/edit")
    public ModelAndView update(WebRequest request, @PathVariable String type, @PathVariable String name, RedirectAttributes redirectAttributes) {
        MessageDto messageDto = MessageDto.builder()
                .name(name)
                .type(MessageType.valueOf(type.toUpperCase()))
                .title(request.getParameter("title"))
                .description(request.getParameter("description"))
                .link(request.getParameter("link"))
                .build();

        messageService.update(messageDto);

        return redirectView(redirectAttributes)
                .withSuccessMessage("Saved")
                .url("/messages/" + type)
                .build();
    }

    @PostMapping("/messages/{type}/{name}/delete")
    public ModelAndView delete(final Model model, @PathVariable String type, @PathVariable String name, RedirectAttributes redirectAttributes) {
        messageService.delete(MessageType.valueOf(type.toUpperCase()), name);

        return redirectView(redirectAttributes)
                .withSuccessMessage("Deleted")
                .url("/messages/" + type)
                .build();
    }

}
