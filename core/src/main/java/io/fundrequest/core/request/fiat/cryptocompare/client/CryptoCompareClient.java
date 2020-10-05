package io.fundrequest.core.request.fiat.cryptocompare.client;

import io.fundrequest.core.request.fiat.cryptocompare.dto.PriceResultDto;

public interface CryptoCompareClient {

    PriceResultDto getPrice(final String symbol);

}
