package io.fundrequest.core.request.fiat;

import io.fundrequest.core.request.fiat.cryptocompare.service.CryptoCompareService;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FiatServiceTest {

    private CryptoCompareService cryptoCompareService;
    private FiatService fiatService;

    @Before
    public void setUp() {
        cryptoCompareService = mock(CryptoCompareService.class);
        fiatService = new FiatService(cryptoCompareService);
    }

    @Test
    public void getUsdPriceOne() {
        TotalFundDto totalFund = TotalFundDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(BigDecimal.TEN).build();
        when(cryptoCompareService.getCurrentPriceInUsd("FND")).thenReturn(0.56);

        double result = fiatService.getUsdPrice(totalFund);

        assertThat(result).isEqualTo(5.6);
    }

    @Test
    public void getUsdPriceMultiple() {
        TotalFundDto totalFund1 = TotalFundDto.builder().tokenAddress("0x0").tokenSymbol("FND").totalAmount(new BigDecimal("8457.858")).build();
        TotalFundDto totalFund2 = TotalFundDto.builder().tokenAddress("0x0").tokenSymbol("ZRX").totalAmount(new BigDecimal("123.464")).build();
        when(cryptoCompareService.getCurrentPriceInUsd("FND")).thenReturn(0.56);
        when(cryptoCompareService.getCurrentPriceInUsd("ZRX")).thenReturn(0.98);

        double result = fiatService.getUsdPrice(totalFund1, totalFund2);

        assertThat(result).isEqualTo(4857.3952);
    }
}