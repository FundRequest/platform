package io.fundrequest.core.request.fund.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.hibernate.validator.constraints.NotEmpty;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static io.fundrequest.core.web3j.EthUtil.toWei;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Data
@NoArgsConstructor
public class CreateERC67FundRequest {

    private static final String DELIMETER_APPROVE_AND_CALL = "|AAC|";

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
    @Min(0)
    private int decimals;

    @Builder
    public CreateERC67FundRequest(String platform, String platformId, BigInteger amount, String fundrequestAddress, String tokenAddress, int decimals) {
        this.platform = platform;
        this.platformId = platformId;
        this.amount = amount;
        this.fundrequestAddress = fundrequestAddress;
        this.tokenAddress = tokenAddress;
        this.decimals = decimals;
    }

    public String toByteData() {
        final Function function = new Function(
                "approveAndCall",
                asList(new org.web3j.abi.datatypes.Address(fundrequestAddress),
                       new org.web3j.abi.datatypes.generated.Uint256(getRawValue()),
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
        builder.append("uint256 ").append(getRawValue().toString()).append(", ");
        builder.append("bytes ").append("0x").append(Hex.encodeHexString((getData()).getBytes()));
        builder.append(")");
        return builder.toString();
    }

    private BigInteger getRawValue() {
        return toWei(amount, decimals);
    }
}
