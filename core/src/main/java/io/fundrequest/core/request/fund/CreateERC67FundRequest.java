package io.fundrequest.core.request.fund;

import org.apache.commons.codec.binary.Hex;
import org.hibernate.validator.constraints.NotEmpty;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class CreateERC67FundRequest {

    public static final String DELIMETER_APPROVE_AND_CALL = "|AAC|";
    @NotEmpty
    private String platform;
    @NotEmpty
    private String platformId;
    @Min(0)
    private BigInteger amount;
    @NotNull
    private String fundrequestAddress;
    @NotNull
    private String tokenAddress;

    public CreateERC67FundRequest() {
    }

    public String toByteData() {
        final Function function = new Function(
                "approveAndCall",
                asList(new org.web3j.abi.datatypes.Address(fundrequestAddress),
                       new org.web3j.abi.datatypes.generated.Uint256(amount),
                       new DynamicBytes(getData().getBytes())),
                emptyList());
        return FunctionEncoder.encode(function);
    }

    private String getData() {
        return platform + DELIMETER_APPROVE_AND_CALL + platformId;
    }

    public String toFunction() {
        final StringBuilder builder = new StringBuilder("approveAndCall").append("(");
        builder.append("address ").append(fundrequestAddress).append(", ");
        builder.append("uint256 ").append(amount.toString()).append(", ");
        builder.append("bytes ").append("0x").append(Hex.encodeHexString((getData()).getBytes()));
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
