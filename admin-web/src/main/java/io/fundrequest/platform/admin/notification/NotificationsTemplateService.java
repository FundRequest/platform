package io.fundrequest.platform.admin.notification;

import io.fundrequest.core.request.RequestService;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;

@Component
public class NotificationsTemplateService {

    private final ITemplateEngine githubTemplateEngine;
    private final RequestService requestService;

    public NotificationsTemplateService(final ITemplateEngine githubTemplateEngine, final RequestService requestService) {
        this.githubTemplateEngine = githubTemplateEngine;
        this.requestService = requestService;
    }


}