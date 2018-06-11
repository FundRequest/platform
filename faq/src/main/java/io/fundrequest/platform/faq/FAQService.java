package io.fundrequest.platform.faq;

import io.fundrequest.platform.faq.model.FaqItemsDto;

public interface FAQService {
    FaqItemsDto getFAQsForPage(String pageName);
}
