package io.fundrequest.platform.admin.notification;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationsTemplateServiceTest {

    private NotificationsTemplateService service;
    private ITemplateEngine githubTemplateEngine;
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        githubTemplateEngine = mock(ITemplateEngine.class);
        requestService = mock(RequestService.class);
        service = new NotificationsTemplateService(githubTemplateEngine, requestService);
    }

    @Test
    void generateMailTemplate() {
        final String template = "ghfjgkhj";
        final List<String> projects = Arrays.asList("sfgsg", "dsgdg");
        final List<String> technologies = Arrays.asList("gsff", "adsgfsg");
        final long lastUpdatedSinceDays = 3L;
        final List<RequestDto> requests = Arrays.asList(RequestDtoMother.fundRequestArea51(), RequestDtoMother.freeCodeCampNoUserStories());
        final ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);

        when(requestService.findAllFor(projects, technologies, lastUpdatedSinceDays)).thenReturn(requests);
        when(githubTemplateEngine.process(eq("notification-templates/open-requests"), any(Context.class))).thenReturn(template);

        final String result = service.generateOpenRequestsMailTemplateFor(projects, technologies, lastUpdatedSinceDays);

        assertThat(result).isEqualTo(template);
        verify(githubTemplateEngine).process(eq("notification-templates/open-requests"), contextCaptor.capture());
        assertThat(contextCaptor.getValue().getVariable("requests")).isEqualTo(requests);
    }
}
