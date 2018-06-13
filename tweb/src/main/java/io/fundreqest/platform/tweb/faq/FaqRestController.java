package io.fundreqest.platform.tweb.faq;

import io.fundrequest.platform.faq.FAQService;
import io.fundrequest.platform.faq.model.FaqItemsDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/faq")
public class FaqRestController {

    private FAQService faqService;

    public FaqRestController(final FAQService faqService) {
        this.faqService = faqService;
    }

    @RequestMapping("/{pageName}")
    public FaqItemsDto faq(@PathVariable String pageName) {
        return faqService.getFAQsForPage(pageName);
    }
}

