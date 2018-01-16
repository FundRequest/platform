package io.fundrequest.core.request.fund.command;


import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateERC67FundRequestTest {

    @Test
    public void toFunction() throws Exception {
        CreateERC67FundRequest github = new CreateERC67FundRequest()
                .setAmount(new BigInteger("100000000000000000000"))
                .setFundrequestAddress("0x00000000000000000000000000000000deadbeef")
                .setTokenAddress("0x0000000000000000000000000000000000000000")
                .setUrl("https://fundrequest.io")
                .setPlatform("github")
                .setPlatformId("1");

        final String function = github.toFunction();
        assertThat(function).isEqualTo("approveAndCall(address 0x00000000000000000000000000000000deadbeef, uint256 100000000000000000000, bytes 0x6769746875627c317c68747470733a2f2f66756e64726571756573742e696f)");
    }
}