package io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CmcTickerResultTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void testJsonMapping() throws IOException {
        String json = "{"
                      + "    \"data\": {"
                      + "        \"id\": 2751, "
                      + "        \"name\": \"FundRequest\", "
                      + "        \"symbol\": \"FND\", "
                      + "        \"website_slug\": \"fundrequest\", "
                      + "        \"rank\": 695, "
                      + "        \"circulating_supply\": 31356053.0, "
                      + "        \"total_supply\": 98611464.0, "
                      + "        \"max_supply\": null, "
                      + "        \"quotes\": {"
                      + "            \"USD\": {"
                      + "                \"price\": 0.0949718, "
                      + "                \"volume_24h\": 43837.4, "
                      + "                \"market_cap\": 2977941.0, "
                      + "                \"percent_change_1h\": -2.32, "
                      + "                \"percent_change_24h\": 1.14, "
                      + "                \"percent_change_7d\": 9.21"
                      + "            }"
                      + "        }, "
                      + "        \"last_updated\": 1529669087"
                      + "    }, "
                      + "    \"metadata\": {"
                      + "        \"timestamp\": 1529668995, "
                      + "        \"error\": null"
                      + "    }"
                      + "}";

        CmcTickerResult result = objectMapper
                .readValue(json, CmcTickerResult.class);

        assertThat(result.getData().getQuotes().getUsd().getPrice()).isEqualTo(0.0949718);


    }
}