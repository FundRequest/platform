package io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmcTickerQuote {
    private Double price;


}
