package io.fundrequest.platform.admin.mails;

import io.fundrequest.common.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
public class OpenRequestsNotificationsController extends AbstractController {

    private final RequestService requestService;
    private final NotificationsTemplateService notificationsTemplateService;

    public OpenRequestsNotificationsController(final NotificationsTemplateService notificationsTemplateService, final RequestService requestService) {
        this.notificationsTemplateService = notificationsTemplateService;
        this.requestService = requestService;
    }

    @GetMapping("/notifications/open-requests")
    public ModelAndView showGenerateTemplateForm(final Model model) {
        return modelAndView(model).withView("notifications/open-requests")
                                  .withObject("projects", requestService.findAllProjects())
                                  .withObject("technologies", requestService.findAllTechnologies())
                                  .build();
    }

    @GetMapping("/notifications/open-requests/template")
    public ModelAndView showGeneratedTemplate(final Model model,
                                       @RequestParam final List<String> projects,
                                       @RequestParam final List<String> technologies,
                                       @RequestParam(name = "last-updated", required = false) String lastUpdated) {
        final String template = notificationsTemplateService.generateMailTemplate();
        return modelAndView(model).withView("notifications/open-requests")
                                  .withObject("projects", requestService.findAllProjects())
                                  .withObject("technologies", requestService.findAllTechnologies())
                                  .withObject("template", template)
                                  .withObject("selectedProjects", projects)
                                  .withObject("selectedTechnologies", technologies)
                                  .withObject("lastUpdated", lastUpdated)
                                  .build();
    }
}
