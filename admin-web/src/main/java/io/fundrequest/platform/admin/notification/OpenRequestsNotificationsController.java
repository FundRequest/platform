package io.fundrequest.platform.admin.notification;

import io.fundrequest.common.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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
                                              @RequestParam(name = "last-updated", required = false) String lastUpdatedSinceDays) {
        final String template = notificationsTemplateService.generateOpenRequestsMailTemplateFor(projects, technologies, Optional.ofNullable(lastUpdatedSinceDays)
                                                                                                                                 .map(Long::valueOf)
                                                                                                                                 .orElse(0L));
        return modelAndView(model).withView("notifications/open-requests")
                                  .withObject("projects", requestService.findAllProjects())
                                  .withObject("technologies", requestService.findAllTechnologies())
                                  .withObject("template", template)
                                  .withObject("selectedProjects", projects)
                                  .withObject("selectedTechnologies", technologies)
                                  .withObject("lastUpdated", lastUpdatedSinceDays)
                                  .build();
    }
}
