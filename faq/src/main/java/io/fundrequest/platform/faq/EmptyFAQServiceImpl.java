package io.fundrequest.platform.faq;

import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.model.FaqItemsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class EmptyFAQServiceImpl implements FAQService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubFAQServiceImpl.class);
    private static final String DUMMY_FAQS_SUBTITLE = "DUMMY SUBTITLE";
    private static final ArrayList<FaqItemDto> DUMMY_FAQ_ITEMS = new ArrayList<>();

    public EmptyFAQServiceImpl() {
        LOGGER.info("EmptyFAQServiceImpl is configured to be used");
    }

    public FaqItemsDto getFAQsForPage(final String pageName) {
        return new FaqItemsDto(DUMMY_FAQS_SUBTITLE, DUMMY_FAQ_ITEMS);
    }
}
