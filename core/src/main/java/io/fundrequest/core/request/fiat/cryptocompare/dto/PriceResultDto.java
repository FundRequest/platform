package io.fundrequest.core.request.fiat.cryptocompare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResultDto {

    @JsonProperty("EUR")
    private String EUR;
    @JsonProperty("USD")
    private String USD;
    @JsonProperty("BTC")
    private String BTC;
    @JsonProperty("ETH")
    private String ETH;
}
