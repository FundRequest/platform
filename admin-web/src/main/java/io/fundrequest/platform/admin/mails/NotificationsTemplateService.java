package io.fundrequest.platform.admin.mails;

import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;

@Component
public class NotificationsTemplateService {


    private final ITemplateEngine githubTemplateEngine;

    public NotificationsTemplateService(final ITemplateEngine githubTemplateEngine) {
        this.githubTemplateEngine = githubTemplateEngine;
    }

    public String generateMailTemplate() {
        final Context context = new Context();
        context.setVariable("requests", Arrays.asList("1", "2", "3"));
        return githubTemplateEngine.process("mail-templates/open-requests", context);
    }
}
