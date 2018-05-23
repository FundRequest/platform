package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundreqest.platform.tweb.fund.FundController;
import io.fundreqest.platform.tweb.request.RequestController;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import io.fundrequest.platform.faq.FAQService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Iniitalizer {

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initialize(event.getApplicationContext());
    }

    private void initialize(ApplicationContext context) {
        initializeRequests(context);
        initializeTechnologies(context);
        initializeProjects(context);
        initializeStatistics(context);
        initializeFaqs(context);
    }

    private void initializeFaqs(ApplicationContext context) {
        FAQService faqService = context.getBean(FAQService.class);
        faqService.getFAQsForPage(RequestController.FAQ_REQUEST_DETAIL_PAGE);
        faqService.getFAQsForPage(RequestController.FAQ_REQUESTS_PAGE);
        faqService.getFAQsForPage(FundController.FAQ_FUND_GITHUB);
    }

    private void initializeStatistics(ApplicationContext context) {
        context.getBean(StatisticsService.class).getStatistics();
    }

    private void initializeProjects(ApplicationContext context) {
        context.getBean(RequestService.class).findAllProjects();
    }

    private void initializeTechnologies(ApplicationContext context) {
        context.getBean(RequestService.class).findAllTechnologies();
    }

    private void initializeRequests(ApplicationContext context) {
        context.getBean(RequestService.class).findAll();
    }

}
