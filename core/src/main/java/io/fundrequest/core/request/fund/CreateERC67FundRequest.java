package io.fundrequest.core.request.fund;

import org.apache.commons.codec.binary.Hex;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CreateERC67FundRequest {

    @NotEmpty
    private String platform;
    @NotEmpty
    private String platformId;
    @NotEmpty
    private String url;
    private BigInteger amount;
    @NotNull
    private String fundrequestAddress;
    @NotNull
    private String tokenAddress;

    public CreateERC67FundRequest() {
    }

    public String toFunction() {
        final StringBuilder builder = new StringBuilder("approveAndCall").append("(");
        builder.append("address ").append(fundrequestAddress).append(", ");
        builder.append("uint256 ").append(amount.toString()).append(", ");
        builder.append("bytes ").append("0x").append(Hex.encodeHexString((platform + "|" + platformId + "|" + url).getBytes()));
        builder.append(")");
        return builder.toString();
    }

    public String getPlatform() {
        return platform;
    }

    public CreateERC67FundRequest setPlatform(final String platform) {
        this.platform = platform;
        return this;
    }

    public String getPlatformId() {
        return platformId;
    }

    public CreateERC67FundRequest setPlatformId(final String platformId) {
        this.platformId = platformId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CreateERC67FundRequest setUrl(final String url) {
        this.url = url;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public CreateERC67FundRequest setAmount(final BigInteger amount) {
        this.amount = amount;
        return this;
    }

    public String getFundrequestAddress() {
        return fundrequestAddress;
    }

    public CreateERC67FundRequest setFundrequestAddress(final String fundrequestAddress) {
        this.fundrequestAddress = fundrequestAddress;
        return this;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public CreateERC67FundRequest setTokenAddress(final String tokenAddress) {
        this.tokenAddress = tokenAddress;
        return this;
    }
}
