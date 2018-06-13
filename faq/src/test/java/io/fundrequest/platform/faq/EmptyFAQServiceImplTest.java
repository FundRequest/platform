package io.fundrequest.platform.faq;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmptyFAQServiceImplTest {

    @Test
    void getFAQsForPage() {
        assertThat(new EmptyFAQServiceImpl().getFAQsForPage("").getFaqItems()).isEmpty();
    }
}