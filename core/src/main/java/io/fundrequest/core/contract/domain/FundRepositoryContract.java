package io.fundrequest.core.contract.domain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

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

    public RemoteCall<BigInteger> getFundedTokenCount(final String platform, final String platformId) {
        final Function function = new Function("getFundedTokenCount",
                Arrays.asList(
                        new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                        new Utf8String(platformId)

                ),
                singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getFundedToken(final String platform, final String platformId, final long index) {
        final Function function = new Function("getFundedTokensByIndex",
                Arrays.asList(
                        new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                        new Utf8String(platformId),
                        new Uint(BigInteger.valueOf(index))

                ),
                singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<BigInteger> balance(final String platform, final String platformId, final String token) {
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
}
