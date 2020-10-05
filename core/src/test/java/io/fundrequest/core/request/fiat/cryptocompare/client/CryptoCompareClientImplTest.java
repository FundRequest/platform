package io.fundrequest.core.request.fiat.cryptocompare.client;

import io.fundrequest.core.request.fiat.cryptocompare.dto.PriceResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class CryptoCompareClientImplTest {

    private CryptoCompareClientImpl client;

    @BeforeEach
    void setUp() {
        client = new CryptoCompareClientImpl("the_api_key");
    }

    @Test
    void getPrice() {
        PriceResultDto result = client.getPrice("DAI");

        System.out.println(result);
    }
}
