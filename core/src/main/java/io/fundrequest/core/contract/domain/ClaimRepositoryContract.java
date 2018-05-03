package io.fundrequest.core.contract.domain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import static io.fundrequest.core.web3j.AddressUtils.prettify;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;

public class ClaimRepositoryContract extends Contract {

    public ClaimRepositoryContract(final String contractAddress, final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ONE, BigInteger.ZERO);
    }

    public Optional<String> getSolver(final String platform, final String platformId) {

        final Function function = new Function("getSolver",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId)

                                                            ),
                                               singletonList(new TypeReference<Utf8String>() {
                                               }));
        try {
            return Optional.of(executeCallSingleValueReturn(function, String.class));
        } catch (final Exception exc) {
            return empty();
        }
    }

    public Optional<String> getTokenByIndex(final String platform, final String platformId, final long index) {
        final Function function = new Function("getTokenByIndex",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId),
                                                       new Uint(BigInteger.valueOf(index))),
                                               singletonList(new TypeReference<Address>() {
                                               }));
        try {
            return Optional.of(prettify(executeCallSingleValueReturn(function, String.class)));
        } catch (final Exception ex) {
            return empty();
        }
    }

    public BigInteger getAmountByToken(final String platform, final String platformId, final String token) {
        final Function function = new Function("getAmountByToken",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId),
                                                       new Address(token)),
                                               singletonList(new TypeReference<Uint256>() {
                                               }));
        try {
            return executeCallSingleValueReturn(function, BigInteger.class);
        } catch (final Exception ex) {
            return BigInteger.ZERO;
        }
    }

    public Long getTokenCount(final String platform, final String platformId) {
        final Function function = new Function("getTokenCount",
                                               Arrays.asList(
                                                       new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                       new Utf8String(platformId)),
                                               singletonList(new TypeReference<Uint>() {
                                               }));
        try {
            return executeCallSingleValueReturn(function, BigInteger.class).longValue();
        } catch (final Exception ex) {
            return 0L;
        }
    }
}
