package io.fundrequest.platform.admin.mails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class NotificationsTemplateServiceTest {

    private NotificationsTemplateService service;
    private ITemplateEngine githubTemplateEngine;

    @BeforeEach
    void setUp() {
        githubTemplateEngine = mock(TemplateEngine.class);
        service = new NotificationsTemplateService(githubTemplateEngine);
    }

    @Test
    public void generateMailTemplate() {
        final String template = "ghfjgkhj";
        final Context context = new Context();
        context.setVariable("requests", Arrays.asList("1", "2", "3"));

        doReturn(template).when(githubTemplateEngine).process("open-requests", context);

        final String result = service.generateMailTemplate();

        assertThat(result).isEqualTo(template);
    }
}
