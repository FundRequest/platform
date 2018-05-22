package io.fundrequest.platform.faq;

import io.fundrequest.platform.faq.model.FaqItem;

import java.util.List;

public interface FAQService {
    List<FaqItem> getFAQsForPage(String pageName);
}
