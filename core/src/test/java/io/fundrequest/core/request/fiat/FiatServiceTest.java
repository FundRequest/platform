package io.fundrequest.core.request.fiat;

import io.fundrequest.core.request.fiat.coinmarketcap.service.CoinMarketCapService;
import io.fundrequest.core.request.fiat.cryptocompare.service.CryptoCompareService;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FiatServiceTest {

    private CryptoCompareService cryptoCompareService;
    private CoinMarketCapService coinMarketCapService;
    private FiatService fiatService;

    @Before
    public void setUp() {
        cryptoCompareService = mock(CryptoCompareService.class);
        coinMarketCapService = mock(CoinMarketCapService.class);
        fiatService = new FiatService(cryptoCompareService, coinMarketCapService);
    }


    @Test
    public void getUsdPriceOneCryptoCompare() {
        TokenValueDto totalFund = TokenValueDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(BigDecimal.TEN).build();
        when(coinMarketCapService.getCurrentPriceInUsd("FND")).thenReturn(Optional.empty());
        when(cryptoCompareService.getCurrentPriceInUsd("FND")).thenReturn(Optional.of(0.56));

        double result = fiatService.getUsdPrice(totalFund);

        assertThat(result).isEqualTo(5.6);
    }

    @Test
    public void getUsdPriceMultipleCryptoCompare() {
        TokenValueDto totalFund1 = TokenValueDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(new BigDecimal("8457.858")).build();
        TokenValueDto totalFund2 = TokenValueDto.builder().tokenAddress("0x0").tokenSymbol("ZRX").totalAmount(new BigDecimal("123.464")).build();
        when(coinMarketCapService.getCurrentPriceInUsd("FND")).thenReturn(Optional.empty());
        when(coinMarketCapService.getCurrentPriceInUsd("ZRX")).thenReturn(Optional.empty());
        when(cryptoCompareService.getCurrentPriceInUsd("FND")).thenReturn(Optional.of(0.56));
        when(cryptoCompareService.getCurrentPriceInUsd("ZRX")).thenReturn(Optional.of(0.98));

        double result = fiatService.getUsdPrice(totalFund1, totalFund2);

        assertThat(result).isEqualTo(4857.3952);
    }

    @Test
    public void coinMarketCapHasPriority() {
        TokenValueDto totalFund = TokenValueDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(BigDecimal.TEN).build();
        when(coinMarketCapService.getCurrentPriceInUsd("FND")).thenReturn(Optional.of(0.56));

        double result = fiatService.getUsdPrice(totalFund);

        assertThat(result).isEqualTo(5.6);

        verifyZeroInteractions(cryptoCompareService);
    }

    @Test
    public void cryptoCompareIsFallback() {
        TokenValueDto totalFund = TokenValueDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(BigDecimal.TEN).build();
        when(coinMarketCapService.getCurrentPriceInUsd("FND")).thenReturn(Optional.empty());
        when(cryptoCompareService.getCurrentPriceInUsd("FND")).thenReturn(Optional.of(0.56));

        double result = fiatService.getUsdPrice(totalFund);

        assertThat(result).isEqualTo(5.6);
        verify(coinMarketCapService).getCurrentPriceInUsd("FND");
        verify(cryptoCompareService).getCurrentPriceInUsd("FND");
    }
}