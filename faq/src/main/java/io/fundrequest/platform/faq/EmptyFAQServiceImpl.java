package io.fundrequest.platform.faq;

import io.fundrequest.platform.faq.model.FaqItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EmptyFAQServiceImpl implements FAQService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubFAQServiceImpl.class);
    private static final ArrayList<FaqItemDto> DUMMY_FAQ_ITEMS = new ArrayList<>();

    public EmptyFAQServiceImpl() {
        LOGGER.info("EmptyFAQServiceImpl is configured to be used");
    }

    public List<FaqItemDto> getFAQsForPage(final String pageName) {
        return DUMMY_FAQ_ITEMS;
    }
}
