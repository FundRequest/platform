package io.fundrequest.core.contract.domain;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Slf4j
public class FundRepositoryContract extends Contract {

    public FundRepositoryContract(final String contractAddress, final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ONE, BigInteger.ZERO);
    }

    public RemoteCall<BigInteger> requestsFunded() {
        final Function function = new Function("requestsFunded",
                                               emptyList(),
                                               singletonList(new TypeReference<Uint256>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> funder(final String address) {
        final Function function = new Function("funders",
                                               Arrays.asList(
                                                       new Address(address)
                                                            ),
                                               singletonList(new TypeReference<Bool>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<List<Type>> getFundInfo(final String platform, final String platformId, final String funder, final String token) {
        final Function getFundInfo = new Function("getFundInfo",
                                                  Arrays.asList(
                                                          new Bytes32(platform.getBytes()),
                                                          new Utf8String(platformId),
                                                          new Address(funder),
                                                          new Address(token)
                                                               ),
                                                  Arrays.asList(
                                                          new TypeReference<Uint256>() {
                                                          },
                                                          new TypeReference<Uint256>() {
                                                          },
                                                          new TypeReference<Uint256>() {
                                                          }
                                                               )
        );
        return executeRemoteCallMultipleValueReturn(getFundInfo);
    }

    public RemoteCall<BigInteger> getFundedTokenCountAsync(final String platform, final String platformId) {

        final Function function = new Function("getFundedTokenCount",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId)

                                                            ),
                                               singletonList(new TypeReference<Uint256>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public Long getFundedTokenCount(final String platform, final String platformId) {
        try {
            return getFundedTokenCountAsync(platform, platformId).send().longValue();
        } catch (final Exception ex) {
            log.error("Unable to fetch token count information", ex);
            return 0L;
        }
    }

    public RemoteCall<String> getFundedTokenAsync(final String platform, final String platformId, final long index) {
        final Function function = new Function("getFundedTokensByIndex",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId),
                                                       new Uint(BigInteger.valueOf(index))

                                                            ),
                                               singletonList(new TypeReference<Address>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public Optional<String> getFundedToken(final String platform, final String platformId, final long index) {
        try {
            return Optional.ofNullable(getFundedTokenAsync(platform, platformId, index).send());
        } catch (Exception e) {
            log.error("Error while trying to fetch a funded token", e);
            return Optional.empty();
        }
    }

    public RemoteCall<Long> getFunderCount(final String platform, final String platformId) {
        final Function function = new Function("getFunderCount",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId)
                                                            ),
                                               singletonList(new TypeReference<Uint>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, Long.class);
    }

    public RemoteCall<BigInteger> amountFunded(final String platform, final String platformId, final String funder, final String token) {
        final Function function = new Function("amountFunded",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId),
                                                       new Address(funder),
                                                       new Address(token)
                                                            ),
                                               singletonList(new TypeReference<Uint256>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceAsync(final String platform, final String platformId, final String token) {
        final Function function = new Function("balance",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId),
                                                       new Address(token)
                                                            ),
                                               singletonList(new TypeReference<Uint256>() {
                                               }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger balance(final String platform, final String platformId, final String token) {
        try {
            return balanceAsync(platform, platformId, token).send();
        } catch (Exception e) {
            log.debug("Error while trying to fetch balance", e);
            return BigInteger.ZERO;
        }
    }
}
