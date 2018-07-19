package io.fundrequest.platform.admin.notification;

import io.fundrequest.core.request.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.ITemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
    void test() {
        assertThat("test something").isEqualTo("test something");
    }
}