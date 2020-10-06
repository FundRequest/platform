package io.fundrequest.core.request.fiat.coinmarketcap.dto.listing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmcQuote {
    @JsonProperty("USD")
    private CmcQuotePrice usd;


}
