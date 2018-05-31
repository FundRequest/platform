package io.fundrequest.core.request.erc67;

import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.fundrequest.core.web3j.EthUtil.toWei;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Component
public class Erc67Generator {

    private static final String DELIMETER_APPROVE_AND_CALL = "|AAC|";


    private TokenInfoService tokenInfoService;
    private String fundRequestContractAddress;

    public Erc67Generator(TokenInfoService tokenInfoService, final @Value("${io.fundrequest.contract.fund-request.address}") String fundRequestContractAddress) {
        this.tokenInfoService = tokenInfoService;
        this.fundRequestContractAddress = fundRequestContractAddress;
    }

    public String toByteData(CreateERC67FundRequest erc67FundRequest) {
        TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(erc67FundRequest.getTokenAddress());
        final Function function = new Function(
                "approveAndCall",
                asList(new org.web3j.abi.datatypes.Address(fundRequestContractAddress),
                       new org.web3j.abi.datatypes.generated.Uint256(getRawValue(erc67FundRequest.getAmount(), tokenInfo.getDecimals())),
                       new DynamicBytes(getData(erc67FundRequest).getBytes())),
                emptyList());
        return FunctionEncoder.encode(function);
    }

    private String getData(CreateERC67FundRequest erc67FundRequest) {
        return erc67FundRequest.getPlatform() + DELIMETER_APPROVE_AND_CALL + erc67FundRequest.getPlatformId();
    }

    public String toFunction(CreateERC67FundRequest erc67FundRequest) {
        TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(erc67FundRequest.getTokenAddress());
        final StringBuilder builder = new StringBuilder("approveAndCall").append("(");
        builder.append("address ").append(fundRequestContractAddress).append(", ");
        builder.append("uint256 ").append(getRawValue(erc67FundRequest.getAmount(), tokenInfo.getDecimals()).toString()).append(", ");
        builder.append("bytes ").append("0x").append(Hex.encodeHexString((getData(erc67FundRequest)).getBytes()));
        builder.append(")");
        return builder.toString();
    }

    private BigInteger getRawValue(BigDecimal amount, int decimals) {
        return toWei(amount, decimals).toBigInteger();
    }
}
