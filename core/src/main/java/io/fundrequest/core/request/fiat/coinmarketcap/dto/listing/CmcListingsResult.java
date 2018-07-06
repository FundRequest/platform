package io.fundrequest.core.request.fiat.coinmarketcap.dto.listing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CmcListingsResult {

    @JsonProperty("data")
    private List<CmcListing> listings;

}
