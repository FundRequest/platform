package io.fundrequest.platform.admin.notification;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class NotificationsTemplateService {

    private final ITemplateEngine githubTemplateEngine;
    private final RequestService requestService;

    public NotificationsTemplateService(final ITemplateEngine githubTemplateEngine, final RequestService requestService) {
        this.githubTemplateEngine = githubTemplateEngine;
        this.requestService = requestService;
    }

    public String generateOpenRequestsMailTemplateFor(final List<String> projects, final List<String> technologies, Long lastUpdatedSinceDays) {
        final List<RequestDto> requests = requestService.findAllFor(projects, technologies, lastUpdatedSinceDays);
        final Context context = new Context();
        context.setVariable("requests", requests);
        return githubTemplateEngine.process("notification-templates/open-requests_email", context);
    }
}
