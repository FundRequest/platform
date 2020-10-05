package io.fundrequest.core.request.fiat.coinmarketcap.dto.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmcQuotePrice {
    private Double price;


}
