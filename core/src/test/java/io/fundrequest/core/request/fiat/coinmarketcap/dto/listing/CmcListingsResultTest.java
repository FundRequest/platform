package io.fundrequest.core.request.fiat.coinmarketcap.dto.listing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CmcListingsResultTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void testMapping() throws IOException {
        String json = "{"
                      + "    \"data\": ["
                      + "        {"
                      + "            \"id\": 1, "
                      + "            \"name\": \"Bitcoin\", "
                      + "            \"symbol\": \"BTC\", "
                      + "            \"website_slug\": \"bitcoin\""
                      + "        }, "
                      + "        {"
                      + "            \"id\": 2, "
                      + "            \"name\": \"Litecoin\", "
                      + "            \"symbol\": \"LTC\", "
                      + "            \"website_slug\": \"litecoin\""
                      + "        }, "
                      + "        {"
                      + "            \"id\": 3, "
                      + "            \"name\": \"Namecoin\", "
                      + "            \"symbol\": \"NMC\", "
                      + "            \"website_slug\": \"namecoin\""
                      + "        }, "
                      + "        {"
                      + "            \"id\": 4, "
                      + "            \"name\": \"Terracoin\", "
                      + "            \"symbol\": \"TRC\", "
                      + "            \"website_slug\": \"terracoin\""
                      + "        }, "
                      + "        {"
                      + "            \"id\": 5, "
                      + "            \"name\": \"Peercoin\", "
                      + "            \"symbol\": \"PPC\", "
                      + "            \"website_slug\": \"peercoin\""
                      + "        }"
                      + "    ], "
                      + "    \"metadata\": {"
                      + "        \"timestamp\": 1529668014, "
                      + "        \"num_cryptocurrencies\": 1604, "
                      + "        \"error\": null"
                      + "    }"
                      + "}";

        CmcListingsResult result = objectMapper
                .readValue(json, CmcListingsResult.class);

        assertThat(result.getListings()).hasSize(5);
        assertThat(result.getListings().get(0).getId()).isEqualTo(1);
        assertThat(result.getListings().get(0).getSymbol()).isEqualTo("BTC");
    }
}