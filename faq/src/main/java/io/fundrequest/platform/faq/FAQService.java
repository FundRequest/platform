package io.fundrequest.platform.faq;

import io.fundrequest.platform.faq.model.FaqItemDto;

import java.util.List;

public interface FAQService {
    List<FaqItemDto> getFAQsForPage(String pageName);
}
