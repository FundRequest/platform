package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundreqest.platform.tweb.fund.FundController;
import io.fundreqest.platform.tweb.request.RequestController;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import io.fundrequest.platform.faq.FAQService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InitalizerTest {

    private Initalizer initalizer;

    @BeforeEach
    void setUp() {
        initalizer = new Initalizer();
    }

    @Test
    void initializesCachedData() {
        ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(event.getApplicationContext()).thenReturn(context);
        RequestService requestService = mock(RequestService.class);
        when(context.getBean(RequestService.class)).thenReturn(requestService);
        StatisticsService statisticsService = mock(StatisticsService.class);
        when(context.getBean(StatisticsService.class)).thenReturn(statisticsService);
        FAQService faqService = mock(FAQService.class);
        when(context.getBean(FAQService.class)).thenReturn(faqService);

        initalizer.onApplicationEvent(event);

        verify(requestService).findAll();
        verify(requestService).findAllTechnologies();
        verify(requestService).findAllProjects();
        verify(statisticsService).getStatistics();
        verify(faqService).getFAQsForPage(RequestController.FAQ_REQUEST_DETAIL_PAGE);
        verify(faqService).getFAQsForPage(RequestController.FAQ_REQUESTS_PAGE);
        verify(faqService).getFAQsForPage(FundController.FAQ_FUND_GITHUB);
    }
}