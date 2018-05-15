package io.fundrequest.core.request.fund.command;


import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateERC67FundRequestTest {

    @Test
    public void toFunction() {
        CreateERC67FundRequest github = CreateERC67FundRequest
                .builder()
                .amount(new BigInteger("100"))
                .decimals(18)
                .fundrequestAddress("0x00000000000000000000000000000000deadbeef")
                .tokenAddress("0x0000000000000000000000000000000000000000")
                .platform("github")
                .platformId("1")
                .build();

        final String function = github.toFunction();
        assertThat(function).isEqualTo("approveAndCall(address 0x00000000000000000000000000000000deadbeef, uint256 100000000000000000000, bytes 0x6769746875627c4141437c31)");
    }
}