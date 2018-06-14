package io.fundrequest.platform.faq.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqItemsDto {
    private String subtitle;
    private List<FaqItemDto> faqItems;
}
