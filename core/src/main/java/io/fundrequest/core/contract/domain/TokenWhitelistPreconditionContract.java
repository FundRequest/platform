package io.fundrequest.core.contract.domain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
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
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class TokenWhitelistPreconditionContract extends Contract {

    public TokenWhitelistPreconditionContract(final String contractAddress, final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ONE, BigInteger.ZERO);
    }

    public RemoteCall<BigInteger> amountOftokens() {
        Function function = new Function("amountOfTokens",
                                         emptyList(),
                                         singletonList(new TypeReference<Uint>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> tokenAsync(final BigInteger index) {
        Function function = new Function("tokens",
                                         singletonList(new Uint(index)),
                                         singletonList(new TypeReference<Address>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public Optional<String> token(final BigInteger index) {
        try {
            final String token = tokenAsync(index).send();
            if (token == null || token.isEmpty() || token.equalsIgnoreCase("0x")) {
                return Optional.empty();
            } else {
                return Optional.of(token);
            }
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }

    public RemoteCall<Boolean> isValidAsync(final String platform, final String platformId, final String token) {
        Function function = new Function("isValid",
                                         Arrays.asList(
                                                 new Bytes32(Arrays.copyOfRange(platform.getBytes(), 0, 32)),
                                                 new Utf8String(platformId),
                                                 new Address(token),
                                                 new Uint256(0),
                                                 new Address(token)
                                                      ),
                                         singletonList(new TypeReference<Bool>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public boolean isValid(final String platform, final String platformId, final String token) {
        try {
            return isValidAsync(platform, platformId, token).send();
        } catch (final Exception ex) {
            return true;
        }
    }
}
